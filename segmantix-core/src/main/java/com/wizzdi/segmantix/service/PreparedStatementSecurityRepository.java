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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificRoleTypesPermissionRequired;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificTenantTypesPermissionRequired;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificUserTypePermissionRequired;

public class PreparedStatementSecurityRepository {

    private static final Logger logger = LoggerFactory.getLogger(PreparedStatementSecurityRepository.class);

    private final String allTypesId;
    private final JDBCFieldPathProvider fieldPathProvider;
    private final SecurityLinksExtractor securityLinksExtractor;


    public PreparedStatementSecurityRepository(JDBCFieldPathProvider fieldPathProvider, SecurityLinksExtractor securityLinksExtractor, String allTypesId) {
        this.allTypesId = allTypesId;
        this.fieldPathProvider = fieldPathProvider;
        this.securityLinksExtractor = securityLinksExtractor;

    }


    public static String permissionGroupPredicate(String tableName, String alias,List<Object> parameters, List<IInstanceGroup> instanceGroups, JDBCFieldPathProvider fieldPathProvider, AtomicReference<JDBCFieldPath> instanceGroupPath) {
        if (instanceGroupPath.get() == null) {
            instanceGroupPath.set(fieldPathProvider.getInstanceGroupPath(tableName, alias));
        }
        Set<String> instanceGroupIds = instanceGroups.stream().map(f -> f.getId()).collect(Collectors.toSet());
        return in("", instanceGroupPath.get().path(), instanceGroupIds,parameters);
    }


    public record SecurityPredicates(String predicates, String joins,List<Object> parameters) {
        public static SecurityPredicates empty() {
            return new SecurityPredicates("","", Collections.emptyList());
        }
    }

    public SecurityPredicates getPreparedStatementSecurityPredicates(String table,  Set<String> types, ISecurityContext securityContext) {
        return  getPreparedStatementSecurityPredicates(table,table,null,types,securityContext);
    }
    public SecurityPredicates getPreparedStatementSecurityPredicates(String table, String type, String alias, Set<String> types, ISecurityContext securityContext) {
        if (!SecurityLinksExtractor.requiresSecurityPredicates(securityContext)) {
            return SecurityPredicates.empty();
        }
        List<Object> parameters=new ArrayList<>();
        String creatorIdPath = fieldPathProvider.getCreatorIdPath(table);
        String idPath = fieldPathProvider.getSecurityId(table);
        String tenantIdPath = fieldPathProvider.getTenantIdPath(table);
        String typePath = fieldPathProvider.getTypePath(table);
        types.add(allTypesId);
        String aliasPrefix = alias == null ? "" : "." + alias;
        SecurityPermissions securityPermissions = securityLinksExtractor.getSecurityPermissions(securityContext, types);
        List<String> securityPreds = new ArrayList<>();
        String tenantsFilter = in(aliasPrefix, tenantIdPath, securityContext.tenants().stream().map(f -> f.getId()), parameters);
        AtomicReference<JDBCFieldPath> join = new AtomicReference<>(null);
        securityPreds.add(eq(aliasPrefix, creatorIdPath, (securityContext.user().getId()),parameters));//creator
        //user
        SecurityPermissionEntry<IUser> user = securityPermissions.userPermissions();
        if (!user.allowed().isEmpty()) {
            securityPreds.add(in(aliasPrefix, idPath, user.allowed().stream().map(f -> f.id()),parameters));
        }
        List<SecuredHolder> userDenied = user.denied();
        List<ITenant> allowAllTenantsWithoutDeny = new ArrayList<>(); // in the case of allow all tenants ,and no denies we can improve the query or avoid a bunch of ors and use an IN clause
        if (specificUserTypePermissionRequired(user, userDenied)) {
            Set<String> allowedTypes = user.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
            securityPreds.add(and(
                    in(aliasPrefix, tenantIdPath, securityContext.tenants().stream().map(f -> f.getId()),parameters),
                    user.allowAll() ? truePred() : typePath != null ? in(aliasPrefix, typePath, allowedTypes,parameters) : allowedTypes.contains(type) ? truePred() : falsePred(),
                    userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                    (user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,user.deniedPermissionGroups(), fieldPathProvider, join)))

            ));

        } else {
            if (user.allowAll()) {
                allowAllTenantsWithoutDeny.addAll(securityContext.tenants());
            }
        }
        if (!user.allowedPermissionGroups().isEmpty()) {
            securityPreds.add(permissionGroupPredicate(table, alias, parameters,user.allowedPermissionGroups(), fieldPathProvider, join));
        }
        //role
        for (SecurityPermissionEntry<IRole> role : securityPermissions.rolePermissions()) {
            IRole roleEntity = role.entity();
            ITenant tenant = roleEntity.getTenant();
            if (!role.allowed().isEmpty()) {
                securityPreds.add(and(
                        in(aliasPrefix, idPath, role.allowed().stream().map(f -> f.id()),parameters),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,user.deniedPermissionGroups(), fieldPathProvider, join))
                ));
            }
            if (specificRoleTypesPermissionRequired(role, userDenied, user)) {
                Set<String> roleAllowedTypes = role.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
                List<String> roleDeniedIds = role.denied().stream().map(f -> f.id()).toList();
                securityPreds.add(and(
                        eq(aliasPrefix, tenantIdPath, tenant.getId(),parameters),
                        role.allowAll() ? truePred() : typePath != null ? in(aliasPrefix, typePath, roleAllowedTypes,parameters) : roleAllowedTypes.contains(type) ? truePred() : falsePred(),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                        role.denied().isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds,parameters)),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias,parameters, user.deniedPermissionGroups(), fieldPathProvider, join)),
                        role.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,role.deniedPermissionGroups(), fieldPathProvider, join))


                ));
            } else {
                if (role.allowAll()) {
                    allowAllTenantsWithoutDeny.add(tenant);
                }
            }
            if (!role.allowedPermissionGroups().isEmpty()) {
                securityPreds.add(and(
                        permissionGroupPredicate(table, alias, parameters,role.allowedPermissionGroups(), fieldPathProvider, join),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,user.deniedPermissionGroups(), fieldPathProvider, join))

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
                    in(aliasPrefix, idPath, tenantAllowed,parameters),
                    userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                    roleDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds,parameters)),
                    user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias,parameters, user.deniedPermissionGroups(), fieldPathProvider, join)),
                    rolePermissionGroupDenied.isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,rolePermissionGroupDenied, fieldPathProvider, join))


            ));
        }
        if (!tenantAllowedPermissionGroups.isEmpty()) {
            securityPreds.add(and(
                    permissionGroupPredicate(table, alias, parameters,tenantAllowedPermissionGroups, fieldPathProvider, join),
                    userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                    roleDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds,parameters))

            ));
        }
        for (SecurityPermissionEntry<ITenant> tenant : tenants) {
            if (specificTenantTypesPermissionRequired(tenant, userDenied, roleDenied, user, rolePermissionGroupDenied)) {
                ITenant tenantEntity = tenant.entity();
                Set<String> allowedTenantTypes = tenant.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
                List<String> tenantDeniedIds = tenant.denied().stream().map(f -> f.id()).toList();
                securityPreds.add(and(
                        eq(aliasPrefix, tenantIdPath, tenantEntity.getId(),parameters),
                        tenant.allowAll() ? truePred() : typePath != null ? in(aliasPrefix, typePath, allowedTenantTypes,parameters) : allowedTenantTypes.contains(type) ? truePred() : falsePred(),
                        userDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, userDenied.stream().map(f -> f.id()),parameters)),
                        roleDenied.isEmpty() ? truePred() : not(in(aliasPrefix, idPath, roleDeniedIds,parameters)),
                        tenant.denied().isEmpty() ? truePred() : not(in(aliasPrefix, idPath, tenantDeniedIds,parameters)),
                        user.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,user.deniedPermissionGroups(), fieldPathProvider, join)),
                        rolePermissionGroupDenied.isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,rolePermissionGroupDenied, fieldPathProvider, join)),
                        tenant.deniedPermissionGroups().isEmpty() ? truePred() : not(permissionGroupPredicate(table, alias, parameters,tenant.deniedPermissionGroups(), fieldPathProvider, join))

                ));
            } else {
                if (tenant.allowAll()) {
                    allowAllTenantsWithoutDeny.add(tenant.entity());
                }
            }
        }
        if (!allowAllTenantsWithoutDeny.isEmpty()) {
            securityPreds.add(or(in(aliasPrefix, tenantIdPath, allowAllTenantsWithoutDeny.stream().map(f -> f.getId()),parameters)));
        }


        String joinStatement = Optional.ofNullable(join.get()).map(f -> f.joinStatement()).orElse(null);
        return new SecurityPredicates(and(
                tenantsFilter,
                or(securityPreds)
        ), joinStatement,parameters);

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

    private static String eq(String aliasPrefix, String left, String right,List<Object> parameters) {
        String value = quote(right);
        parameters.add(value);
        return aliasPrefix + left + "= ?" ;
    }

    private static String in(String aliasPrefix, String left, Collection<String> right,List<Object> parameters) {
        List<String> inValues = right.stream().map(f -> quote(f)).toList();
        parameters.add(inValues);
        return aliasPrefix + left + "in (" + getParameterPlaceholder(inValues.size()) + ")";
    }

    private static String getParameterPlaceholder(int size) {
        return IntStream.range(0,size).mapToObj(f->"?").collect(Collectors.joining(","));
    }

    private static String in(String aliasPrefix, String left, Stream<String> right,List<Object> parameters) {
        List<String> inValues = right.map(f -> quote(f)).toList();
        parameters.addAll(inValues);
        return aliasPrefix + left + "in (" + getParameterPlaceholder(inValues.size()) + ")";
    }

    private static String in(String aliasPrefix, String left, String right,List<Object> parameters) {
        return aliasPrefix + left + "in (" + right + ")";
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
