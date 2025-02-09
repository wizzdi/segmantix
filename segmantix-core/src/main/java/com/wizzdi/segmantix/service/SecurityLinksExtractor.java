package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.IRoleSecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.ITenantSecurityLink;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.model.IUserSecurityLink;
import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.JDBCFieldPathProvider;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.api.service.SegmantixCache;
import com.wizzdi.segmantix.internal.SecuredHolder;
import com.wizzdi.segmantix.internal.SecurityPermissionEntry;
import com.wizzdi.segmantix.internal.SecurityPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityLinksExtractor {

    private static final Logger logger= LoggerFactory.getLogger(SecurityLinksExtractor.class);

    private final IOperation allOp;
    private final String allTypesId;
    private final OperationGroupLinkProvider operationGroupLinkProvider;
    private final SecurityLinkProvider securityProvider;
    private final Cache dataAccessControlCache;
    private final Cache operationToOperationGroupCache;

    public SecurityLinksExtractor(OperationGroupLinkProvider operationGroupLinkProvider, SecurityLinkProvider securityProvider,
                                  SegmantixCache segmantixCache, IOperation allOp, String allTypesId) {
        this.allOp = allOp;
        this.allTypesId=allTypesId;
        this.operationGroupLinkProvider = operationGroupLinkProvider;
        this.securityProvider = securityProvider;
        this.dataAccessControlCache = segmantixCache.dataAccessControlCache();
        this.operationToOperationGroupCache = segmantixCache.operationToOperationGroupCache();
    }

    record SecurityHolder(List<IUserSecurityLink> users, List<IRoleSecurityLink> roles,
                          List<ITenantSecurityLink> tenants) {
    }


    public SecurityPermissions getSecurityPermissions(ISecurityContext securityContext, Set<String> types) {
        IUser user=securityContext.user();
        List<? extends IRole> roles = securityContext.roles();
        List<? extends ITenant> tenants=securityContext.tenants();
        IOperation operation=securityContext.operation();
        SecurityHolder securityHolder = getSecurityHolder(securityContext);
        List<IUserSecurityLink> userLinks = securityHolder.users();
        List<IRoleSecurityLink> roleLinks = securityHolder.roles();
        List<ITenantSecurityLink> tenantLinks = securityHolder.tenants();
        Map<String, IRole> allRoles = roles.stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
        Map<String, ITenant> allTenants = tenants.stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
        Set<String> relevantOps = operation!= null ? Set.of(allOp.getId(), operation.getId()) : null;
        Set<String> relevantOpGroups=operation!=null?getRelevantOpGroups(operation):null;
        List<IUserSecurityLink> userLinksForOp = userLinks.stream().filter(f -> filterSecurityForOperation(f, relevantOps, relevantOpGroups)).toList();
        Map<String, List<IRoleSecurityLink>> role = roleLinks.stream().filter(f ->filterSecurityForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getRole().getId()));
        Map<String, List<ITenantSecurityLink>> tenant = tenantLinks.stream().filter(f -> filterSecurityForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getTenant().getId()));
        return new SecurityPermissions(SecurityPermissionEntry.of(user, userLinksForOp,allTypesId,types), role.entrySet().stream().map(f -> SecurityPermissionEntry.of(allRoles.get(f.getKey()), f.getValue(),  allTypesId, types)).toList(), tenant.entrySet().stream().map(f -> SecurityPermissionEntry.of(allTenants.get(f.getKey()), f.getValue(), allTypesId, types)).toList());

    }

    private static boolean filterSecurityForOperation(ISecurityLink f, Set<String> relevantOps, Set<String> relevantOpGroups) {
        return relevantOps == null || relevantOpGroups == null || (f.getOperation() != null && relevantOps.contains(f.getOperation().getId())) || (f.getOperationGroup() != null && relevantOpGroups.contains(f.getOperationGroup().getId()));
    }

    private Set<String> getRelevantOpGroups(IOperation op) {
        return operationToOperationGroupCache.get(op.getId(), () -> operationGroupLinkProvider.listAllOperationGroupLinks(Collections.singletonList(op)).stream().map(f->f.getOperationGroup().getId()).collect(Collectors.toSet()));
    }

    private SecurityHolder getSecurityHolder(ISecurityContext securityContext) {
        IUser user=securityContext.user();
        List<? extends IRole> roles=securityContext.roles();
        List<? extends ITenant> tenants=securityContext.tenants();
        List<IUserSecurityLink> userPermissions = dataAccessControlCache.get(user.getId(), List.class);
        List<List<IRoleSecurityLink>> rolePermissions = roles.stream().map(f -> (List<IRoleSecurityLink>) dataAccessControlCache.get(f.getId(), List.class)).toList();
        List<List<ITenantSecurityLink>> tenantPermissions = tenants.stream().map(f -> (List<ITenantSecurityLink>) dataAccessControlCache.get(f.getId(), List.class)).toList();
        List<IInstanceGroup> cachedInstanceGroups=extractUsedInstanceGroups(userPermissions,rolePermissions,tenantPermissions);
        if (userPermissions != null && rolePermissions.stream().allMatch(f -> f != null) && tenantPermissions.stream().allMatch(f -> f != null)) {
            List<IRoleSecurityLink> roleLinks = rolePermissions.stream().flatMap(f -> f.stream()).toList();
            List<ITenantSecurityLink> tenantLinks = tenantPermissions.stream().flatMap(f -> f.stream()).toList();
            logger.debug("cache hit users: {} , roles: {} , tenants: {} ", userPermissions.size(), roleLinks.size(), tenantLinks.size());
            return new SecurityHolder(userPermissions, roleLinks, tenantLinks);

        }
        List<ISecurityLink> securitys = this.securityProvider.getSecurityLinks(securityContext);
        List<IUserSecurityLink> userLinks = securitys.stream().filter(f -> f instanceof IUserSecurityLink).map(f -> (IUserSecurityLink) f).toList();
        List<IRoleSecurityLink> roleLinks = securitys.stream().filter(f -> f instanceof IRoleSecurityLink).map(f -> (IRoleSecurityLink) f).toList();
        List<ITenantSecurityLink> tenantLinks = securitys.stream().filter(f -> f instanceof ITenantSecurityLink).map(f -> (ITenantSecurityLink) f).toList();
        dataAccessControlCache.put(user.getId(), userLinks);
        for (IRole role : roles) {
            dataAccessControlCache.put(role.getId(), roleLinks.stream().filter(f -> f.getRole().getId().equals(role.getId())).toList());
        }
        for (ITenant tenant : tenants) {
            dataAccessControlCache.put(tenant.getId(), tenantLinks.stream().filter(f -> f.getTenant().getId().equals(tenant.getId())).toList());
        }

        return new SecurityHolder(userLinks, roleLinks, tenantLinks);

    }

    private List<IInstanceGroup> extractUsedInstanceGroups(List<IUserSecurityLink> userPermissions, List<List<IRoleSecurityLink>> rolePermissions, List<List<ITenantSecurityLink>> tenantPermissions) {
        List<IInstanceGroup> toRet = Optional.ofNullable(userPermissions).stream().flatMap(List::stream).map(f -> f.getInstanceGroup()).filter(f->f!=null).collect(Collectors.toList());
        List<IInstanceGroup> role = rolePermissions.stream().filter(f->f!=null).flatMap(List::stream).map(f -> f.getInstanceGroup()).filter(f -> f != null).toList();
        List<IInstanceGroup> tenant = tenantPermissions.stream().filter(f->f!=null).flatMap(List::stream).map(f -> f.getInstanceGroup()).filter(f -> f != null).toList();
        toRet.addAll(role);
        toRet.addAll(tenant);
        return toRet;
    }


    public static boolean specificTenantTypesPermissionRequired(SecurityPermissionEntry<ITenant> tenant, List<SecuredHolder> userDenied, List<SecuredHolder> roleDenied, SecurityPermissionEntry<IUser> user, List<IInstanceGroup> rolePermissionGroupDenied) {
        return !tenant.allowedTypes().isEmpty() && (!tenant.allowAll() || !userDenied.isEmpty() || !roleDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty() || !rolePermissionGroupDenied.isEmpty());
    }

   public  static boolean specificUserTypePermissionRequired(SecurityPermissionEntry<IUser> user, List<SecuredHolder> userDenied) {
        return !user.allowedTypes().isEmpty() && (!user.allowAll() || !userDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty());
    }

    public static boolean specificRoleTypesPermissionRequired(SecurityPermissionEntry<IRole> role, List<SecuredHolder> userDenied, SecurityPermissionEntry<IUser> user) {
        return !role.allowedTypes().isEmpty() && (!role.allowAll() || !userDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty());
    }

    public static boolean requiresSecurityPredicates(ISecurityContext securityContext) {
        if (securityContext == null) {
            return false;
        }
        IUser user = securityContext.user();
        if (user == null) {
            return false;
        }
        List<? extends IRole> roles = securityContext.roles();
        return !isSuperAdmin(roles);
    }


    private static boolean isSuperAdmin(List<? extends IRole> roles) {
        return roles.stream().anyMatch(f -> f.isSuperAdmin());
    }

}
