package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.service.JDBCFieldPath;
import com.wizzdi.segmantix.api.service.JDBCFieldPathProvider;
import com.wizzdi.segmantix.internal.SecuredHolder;
import com.wizzdi.segmantix.internal.SecurityPermissionEntry;
import com.wizzdi.segmantix.internal.SecurityPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificRoleTypesPermissionRequired;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificTenantTypesPermissionRequired;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificUserTypePermissionRequired;

public class SQLSecurityRepository {

    private static final Logger logger = LoggerFactory.getLogger(SQLSecurityRepository.class);

    private final String allTypesId;
    private final JDBCFieldPathProvider fieldPathProvider;
    private final SecurityLinksExtractor securityLinksExtractor;


    public SQLSecurityRepository(JDBCFieldPathProvider fieldPathProvider, SecurityLinksExtractor securityLinksExtractor, String allTypesId) {
        this.allTypesId = allTypesId;
        this.fieldPathProvider = fieldPathProvider;
        this.securityLinksExtractor = securityLinksExtractor;

    }


    public static String permissionGroupPredicate(String tableName, String alias, List<IInstanceGroup> instanceGroups, JDBCFieldPathProvider fieldPathProvider, AtomicReference<JDBCFieldPath> instanceGroupPath) {
        if (instanceGroupPath.get() == null) {
            instanceGroupPath.set(fieldPathProvider.getInstanceGroupPath(tableName, alias));
        }
        Set<String> instanceGroupIds = instanceGroups.stream().map(f -> f.getId()).collect(Collectors.toSet());
        return in("", instanceGroupPath.get().path(), instanceGroupIds);
    }


    public record SecurityPredicates(String predicates, String joins) {
        public static SecurityPredicates empty() {
            return new SecurityPredicates("","");
        }
    }

    public SecurityPredicates getPlainSQLSecurityPredicate(String table, Set<String> types, ISecurityContext securityContext) {
       return getPlainSQLSecurityPredicate(table,table,null,types,securityContext);
    }

    public SecurityPredicates getPlainSQLSecurityPredicate(String table, String type, String alias, Set<String> types, ISecurityContext securityContext) {
        if (!SecurityLinksExtractor.requiresSecurityPredicates(securityContext)) {
            return SecurityPredicates.empty();
        }
        String creatorIdPath = fieldPathProvider.getCreatorIdPath(table);
        String idPath = fieldPathProvider.getSecurityId(table);
        String tenantIdPath = fieldPathProvider.getTenantIdPath(table);
        String typePath = fieldPathProvider.getTypePath(table);
        types.add(allTypesId);
        String aliasPrefix = alias == null ? "" : "." + alias;
        SecurityPermissions securityPermissions = securityLinksExtractor.getSecurityPermissions(securityContext, types);
        List<String> securityPreds = new ArrayList<>();
        AtomicReference<JDBCFieldPath> join = new AtomicReference<>(null);
        securityPreds.add(eq(aliasPrefix, creatorIdPath, (securityContext.user().getId())));//creator
        //user
        SecurityPermissionEntry<IUser> user = securityPermissions.userPermissions();
        if (!user.allowed().isEmpty()) {
            securityPreds.add(in(aliasPrefix, idPath, user.allowed().stream().map(f -> f.id())));
        }
        List<SecuredHolder> userDenied = user.denied();
        List<ITenant> allowAllTenantsWithoutDeny = new ArrayList<>(); // in the case of allow all tenants ,and no denies we can improve the query or avoid a bunch of ors and use an IN clause
        if (specificUserTypePermissionRequired(user, userDenied)) {
            Set<String> allowedTypes = user.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
            securityPreds.add(and(
                    in(aliasPrefix, tenantIdPath, securityContext.tenants().stream().map(f -> f.getId())),
                    user.allowAll() ? truePred() : typePath != null ? in(aliasPrefix, typePath, allowedTypes) : allowedTypes.contains(type) ? truePred() : falsePred(),
                    userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                    (user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, user.deniedPermissionGroups(), fieldPathProvider, join)))

            ));

        } else {
            if (user.allowAll()) {
                allowAllTenantsWithoutDeny.addAll(securityContext.tenants());
            }
        }
        if (!user.allowedPermissionGroups().isEmpty()) {
            securityPreds.add(permissionGroupPredicate(table, alias, user.allowedPermissionGroups(), fieldPathProvider, join));
        }
        //role
        for (SecurityPermissionEntry<IRole> role : securityPermissions.rolePermissions()) {
            IRole roleEntity = role.entity();
            ITenant tenant = roleEntity.getTenant();
            if (!role.allowed().isEmpty()) {
                securityPreds.add(and(
                        in(aliasPrefix, idPath, role.allowed().stream().map(f -> f.id())),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, user.deniedPermissionGroups(), fieldPathProvider, join))
                ));
            }
            if (specificRoleTypesPermissionRequired(role, userDenied, user)) {
                Set<String> roleAllowedTypes = role.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
                List<String> roleDeniedIds = role.denied().stream().map(f -> f.id()).toList();
                securityPreds.add(and(
                        eq(aliasPrefix, tenantIdPath, tenant.getId()),
                        role.allowAll() ? truePred() : typePath != null ? in(aliasPrefix, typePath, roleAllowedTypes) : roleAllowedTypes.contains(type) ? truePred() : falsePred(),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                        role.denied().isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds)),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, user.deniedPermissionGroups(), fieldPathProvider, join)),
                        role.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, role.deniedPermissionGroups(), fieldPathProvider, join))


                ));
            } else {
                if (role.allowAll()) {
                    allowAllTenantsWithoutDeny.add(tenant);
                }
            }
            if (!role.allowedPermissionGroups().isEmpty()) {
                securityPreds.add(and(
                        permissionGroupPredicate(table, alias, role.allowedPermissionGroups(), fieldPathProvider, join),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, user.deniedPermissionGroups(), fieldPathProvider, join))

                ));
            }

        }
        //tenant
        List<SecurityPermissionEntry<ITenant>> tenants = securityPermissions.tenantPermissions();

        List<IInstanceGroup> tenantAllowedPermissionGroups = tenants.stream().map(f -> f.allowedPermissionGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());
        List<String> tenantAllowed = tenants.stream().map(f -> f.allowed()).flatMap(f -> f.stream()).map(f -> f.id()).collect(Collectors.toList());

        List<SecuredHolder> roleDenied = securityPermissions.rolePermissions().stream().map(f -> f.denied()).flatMap(f -> f.stream()).collect(Collectors.toList());

        List<String> roleDeniedIds = roleDenied.stream().map(f -> f.id()).toList();

        List<IInstanceGroup> rolePermissionGroupDenied = securityPermissions.rolePermissions().stream().map(f -> f.deniedPermissionGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());
        if (!tenantAllowed.isEmpty()) {
            securityPreds.add(and(
                    in(aliasPrefix, idPath, tenantAllowed),
                    userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                    roleDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds)),
                    user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, user.deniedPermissionGroups(), fieldPathProvider, join)),
                    rolePermissionGroupDenied.isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, rolePermissionGroupDenied, fieldPathProvider, join))


            ));
        }
        if (!tenantAllowedPermissionGroups.isEmpty()) {
            securityPreds.add(and(
                    permissionGroupPredicate(table, alias, tenantAllowedPermissionGroups, fieldPathProvider, join),
                    userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                    roleDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds))

            ));
        }
        for (SecurityPermissionEntry<ITenant> tenant : tenants) {
            if (specificTenantTypesPermissionRequired(tenant, userDenied, roleDenied, user, rolePermissionGroupDenied)) {
                ITenant tenantEntity = tenant.entity();
                Set<String> allowedTenantTypes = tenant.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
                List<String> tenantDeniedIds = tenant.denied().stream().map(f -> f.id()).toList();
                securityPreds.add(and(
                        eq(aliasPrefix, tenantIdPath, tenantEntity.getId()),
                        tenant.allowAll() ? truePred() : typePath != null ? in(aliasPrefix, typePath, allowedTenantTypes) : allowedTenantTypes.contains(type) ? truePred() : falsePred(),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()))),
                        roleDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds)),
                        tenant.denied().isEmpty() ? truePred() : not(in(aliasPrefix, idPath, tenantDeniedIds)),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, user.deniedPermissionGroups(), fieldPathProvider, join)),
                        rolePermissionGroupDenied.isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, rolePermissionGroupDenied, fieldPathProvider, join)),
                        tenant.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, tenant.deniedPermissionGroups(), fieldPathProvider, join))

                ));
            } else {
                if (tenant.allowAll()) {
                    allowAllTenantsWithoutDeny.add(tenant.entity());
                }
            }
        }
        if (!allowAllTenantsWithoutDeny.isEmpty()) {
            securityPreds.add(or(in(aliasPrefix, tenantIdPath, allowAllTenantsWithoutDeny.stream().map(f -> f.getId()))));
        }


        String joinStatement = Optional.ofNullable(join.get()).map(f -> f.joinStatement()).orElse(null);
        return new SecurityPredicates(and(
                in(aliasPrefix, tenantIdPath, securityContext.tenants().stream().map(f -> f.getId())),
                or(securityPreds)
        ), joinStatement);

    }

    private static String and(String... preds) {
        return "(" + String.join(" and ", preds) + ")";
    }

    private static String or(String... preds) {
        return "(" + String.join(" or ", preds) + ")";
    }

    private static String or(List<String> preds) {
        return "(" + String.join(" or ", preds) + ")";
    }

    private static String not(String pred) {
        return "not(" + pred + ")";
    }

    private static String eq(String aliasPrefix, String left, String right) {
        return aliasPrefix + left + "=" + quote(right);
    }

    private static String in(String aliasPrefix, String left, Collection<String> right) {
        return aliasPrefix + left + " in (" + right.stream().map(f -> quote(f)).collect(Collectors.joining(",")) + ")";
    }

    private static String in(String aliasPrefix, String left, Stream<String> right) {
        return aliasPrefix + left + " in (" + right.map(f -> quote(f)).collect(Collectors.joining(",")) + ")";
    }

    private static String in(String aliasPrefix, String left, String right) {
        return aliasPrefix + left + " in (" + right + ")";
    }

    private String truePred() {
        return "TRUE";
    }

    private String falsePred() {
        return "FALSE";
    }

    private static String quote(String f) {
        return "'" + f + "'";
    }


}
