package com.wizzdi.segmantix.impl.service;

import com.wizzdi.segmantix.api.Cache;
import com.wizzdi.segmantix.api.FieldPathProvider;
import com.wizzdi.segmantix.api.OperationToGroupService;
import com.wizzdi.segmantix.api.PermissionGroupToBaseclassService;
import com.wizzdi.segmantix.api.Secured;
import com.wizzdi.segmantix.api.SecurityLinkService;
import com.wizzdi.segmantix.api.IPermissionGroup;
import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import com.wizzdi.segmantix.api.IRole;
import com.wizzdi.segmantix.api.IRoleToBaseclass;
import com.wizzdi.segmantix.api.ISecurityLink;
import com.wizzdi.segmantix.api.ISecurityOperation;
import com.wizzdi.segmantix.api.ISecurityTenant;
import com.wizzdi.segmantix.api.ISecurityUser;
import com.wizzdi.segmantix.api.ITenantToBaseclass;
import com.wizzdi.segmantix.api.IUserToBaseclass;
import com.wizzdi.segmantix.service.SecuredHolder;
import com.wizzdi.segmantix.service.SecurityContext;
import com.wizzdi.segmantix.service.SecurityPermissionEntry;
import com.wizzdi.segmantix.service.SecurityPermissions;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseclassRepository  {

	private static final Logger logger = LoggerFactory.getLogger(BaseclassRepository.class);

	private final ISecurityOperation allOp;
	private final OperationToGroupService operationToGroupService;
	private final SecurityLinkService securityLinkService;
	private final PermissionGroupToBaseclassService permissionGroupToBaseclassService;
	private final FieldPathProvider fieldPathProvider;
	private final Cache dataAccessControlCache;
	private final Cache operationToOperationGroupCache;

	public BaseclassRepository( FieldPathProvider fieldPathProvider,OperationToGroupService operationToGroupService,SecurityLinkService securityLinkService,PermissionGroupToBaseclassService permissionGroupToBaseclassService,
							    Cache dataAccessControlCache, Cache operationToOperationGroupCache,ISecurityOperation allOp) {
		this.allOp = allOp;
		this.fieldPathProvider=fieldPathProvider;
		this.operationToGroupService = operationToGroupService;
		this.securityLinkService=securityLinkService;
		this.permissionGroupToBaseclassService=permissionGroupToBaseclassService;
		this.dataAccessControlCache = dataAccessControlCache;
		this.operationToOperationGroupCache = operationToOperationGroupCache;
	}

	record SecurityLinkHolder(List<IUserToBaseclass> users, List<IRoleToBaseclass> roles,
							  List<ITenantToBaseclass> tenants,
							  Map<String, List<IPermissionGroupToBaseclass>> permissionGroupToBaseclasses) {
	}


	public SecurityPermissions getSecurityPermissions(SecurityContext securityContext) {
		ISecurityUser user=securityContext.securityUser();
		List<IRole> roles = securityContext.roles();
		List<ISecurityTenant> tenants=securityContext.tenants();
		ISecurityOperation securityOperation=securityContext.securityOperation();
		SecurityLinkHolder securityLinkHolder = getSecurityLinkHolder(securityContext);
		List<IUserToBaseclass> userLinks = securityLinkHolder.users();
		List<IRoleToBaseclass> roleLinks = securityLinkHolder.roles();
		List<ITenantToBaseclass> tenantLinks = securityLinkHolder.tenants();
		Map<String, List<IPermissionGroupToBaseclass>> permissionGroupToBaseclasses = securityLinkHolder.permissionGroupToBaseclasses();
		Map<String, IRole> allRoles = roles.stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Map<String, ISecurityTenant> allTenants = tenants.stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Set<String> relevantOps = securityOperation!= null ? Set.of(allOp.getId(), securityOperation.getId()) : null;
		Set<String> relevantOpGroups=securityOperation!=null?getRelevantOpGroups(securityOperation):null;
		List<IUserToBaseclass> userLinksForOp = userLinks.stream().filter(f -> filterSecurityLinkForOperation(f, relevantOps, relevantOpGroups)).toList();
		Map<String, List<IRoleToBaseclass>> role = roleLinks.stream().filter(f ->filterSecurityLinkForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getRole().getId()));
		Map<String, List<ITenantToBaseclass>> tenant = tenantLinks.stream().filter(f -> filterSecurityLinkForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getTenant().getId()));
		return new SecurityPermissions(SecurityPermissionEntry.of(user, userLinksForOp,permissionGroupToBaseclasses), role.entrySet().stream().map(f -> SecurityPermissionEntry.of(allRoles.get(f.getKey()), f.getValue(), permissionGroupToBaseclasses)).toList(), tenant.entrySet().stream().map(f -> SecurityPermissionEntry.of(allTenants.get(f.getKey()), f.getValue(), permissionGroupToBaseclasses)).toList());

	}

	private static boolean filterSecurityLinkForOperation(ISecurityLink f, Set<String> relevantOps, Set<String> relevantOpGroups) {
		return relevantOps == null || relevantOpGroups == null || (f.getOperation() != null && relevantOps.contains(f.getOperation().getId())) || (f.getOperationGroup() != null && relevantOpGroups.contains(f.getOperationGroup().getId()));
	}

	private Set<String> getRelevantOpGroups(ISecurityOperation op) {
		return operationToOperationGroupCache.get(op.getId(), () -> operationToGroupService.listAllOperationToGroups(Collections.singletonList(op)).stream().map(f->f.getOperationGroup().getId()).collect(Collectors.toSet()));
	}

	private SecurityLinkHolder getSecurityLinkHolder(SecurityContext securityContext) {
		ISecurityUser securityUser=securityContext.securityUser();
		List<IRole> roles=securityContext.roles();
		List<ISecurityTenant> tenants=securityContext.tenants();
		List<IUserToBaseclass> userPermissions = dataAccessControlCache.get(securityUser.getId(), List.class);
		List<List<IRoleToBaseclass>> rolePermissions = roles.stream().map(f -> (List<IRoleToBaseclass>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		List<List<ITenantToBaseclass>> tenantPermissions = tenants.stream().map(f -> (List<ITenantToBaseclass>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		List<IPermissionGroup> cachedPermissionGroups=extractUsedPermissionGroups(userPermissions,rolePermissions,tenantPermissions);
		List<List<IPermissionGroupToBaseclass>> permissionGroupLinkCache = cachedPermissionGroups.stream().map(f -> (List<IPermissionGroupToBaseclass>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		if (userPermissions != null && rolePermissions.stream().allMatch(f -> f != null) && tenantPermissions.stream().allMatch(f -> f != null)&&permissionGroupLinkCache.stream().allMatch(f->f!=null)) {
			List<IRoleToBaseclass> roleLinks = rolePermissions.stream().flatMap(f -> f.stream()).toList();
			List<ITenantToBaseclass> tenantLinks = tenantPermissions.stream().flatMap(f -> f.stream()).toList();
			Map<String,List<IPermissionGroupToBaseclass>> permissionGroupToBaseclasses=permissionGroupLinkCache.stream().flatMap(List::stream).collect(Collectors.groupingBy(f->f.getPermissionGroup().getId()));
			logger.debug("cache hit users: {} , roles: {} , tenants: {} , permission group links: {}", userPermissions.size(), roleLinks.size(), tenantLinks.size(),permissionGroupLinkCache.size());
			return new SecurityLinkHolder(userPermissions, roleLinks, tenantLinks, permissionGroupToBaseclasses);

		}
		List<ISecurityLink> securityLinks = this.securityLinkService.getSecurityLinks(securityContext);
		List<IPermissionGroup> permissionGroups=securityLinks.stream().map(f->f.getPermissionGroup()).filter(f->f!=null).toList();
		Map<String,List<IPermissionGroupToBaseclass>> permissionGroupToBaseclasses=permissionGroups.isEmpty()?Collections.emptyMap():this.permissionGroupToBaseclassService.getPermissionGroupLinks(permissionGroups).stream().collect(Collectors.groupingBy(f->f.getPermissionGroup().getId()));
		List<IUserToBaseclass> userLinks = securityLinks.stream().filter(f -> f instanceof IUserToBaseclass).map(f -> (IUserToBaseclass) f).toList();
		List<IRoleToBaseclass> roleLinks = securityLinks.stream().filter(f -> f instanceof IRoleToBaseclass).map(f -> (IRoleToBaseclass) f).toList();
		List<ITenantToBaseclass> tenantLinks = securityLinks.stream().filter(f -> f instanceof ITenantToBaseclass).map(f -> (ITenantToBaseclass) f).toList();
		dataAccessControlCache.put(securityUser.getId(), userLinks);
		for (IRole role : roles) {
			dataAccessControlCache.put(role.getId(), roleLinks.stream().filter(f -> f.getRole().getId().equals(role.getId())).toList());
		}
		for (ISecurityTenant tenant : tenants) {
			dataAccessControlCache.put(tenant.getId(), tenantLinks.stream().filter(f -> f.getTenant().getId().equals(tenant.getId())).toList());
		}
		for (IPermissionGroup permissionGroup : permissionGroups) {
			List<IPermissionGroupToBaseclass> links=permissionGroupToBaseclasses.getOrDefault(permissionGroup.getId(),Collections.emptyList());
			dataAccessControlCache.put(permissionGroup.getId(), links);
		}
		return new SecurityLinkHolder(userLinks, roleLinks, tenantLinks,permissionGroupToBaseclasses);

	}

	private List<IPermissionGroup> extractUsedPermissionGroups(List<IUserToBaseclass> userPermissions, List<List<IRoleToBaseclass>> rolePermissions, List<List<ITenantToBaseclass>> tenantPermissions) {
		List<IPermissionGroup> toRet = Optional.ofNullable(userPermissions).stream().flatMap(List::stream).map(f -> f.getPermissionGroup()).filter(f->f!=null).collect(Collectors.toList());
		List<IPermissionGroup> role = rolePermissions.stream().filter(f->f!=null).flatMap(List::stream).map(f -> f.getPermissionGroup()).filter(f -> f != null).toList();
		List<IPermissionGroup> tenant = tenantPermissions.stream().filter(f->f!=null).flatMap(List::stream).map(f -> f.getPermissionGroup()).filter(f -> f != null).toList();
		toRet.addAll(role);
		toRet.addAll(tenant);
		return toRet;
	}


	public <T extends Secured> void addBaseclassPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		if (!requiresSecurityPredicates(securityContext)) {
			return;
		}

		SecurityPermissions securityPermissions = getSecurityPermissions(securityContext);
		List<Predicate> securityPreds = new ArrayList<>();
		Path<String> creatorIdPath = fieldPathProvider.getCreatorIdPath(r);
		securityPreds.add(cb.equal(creatorIdPath, securityContext.securityUser().getId()));//creator
		//user
		SecurityPermissionEntry<ISecurityUser> user = securityPermissions.userPermissions();
		Path<String> idPath = fieldPathProvider.getSecurityId(r);
		if (!user.allowed().isEmpty()) {
			securityPreds.add(idPath.in(user.allowed().stream().map(f->f.id()).toList()));
		}
		List<SecuredHolder> userDenied = user.denied();
		List<ISecurityTenant> allowAllTenantsWithoutDeny=new ArrayList<>(); // in the case of allow all tenants ,and no denies we can improve the query or avoid a bunch of ors and use an IN clause
		Path<String> tenantIdPath = fieldPathProvider.getTenantIdPath(r);
		Path<String> typePath = fieldPathProvider.getTypePath(r);
		List<String> userDeniedIds = userDenied.stream().map(f -> f.id()).toList();
		if (specificUserTypePermissionRequired(user,userDenied)) {
			Set<String> allowedTypes = user.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
			securityPreds.add(cb.and(
					tenantIdPath.in(securityContext.tenants().stream().map(f->f.getId()).toList()),
					user.allowAll() ? cb.and() : typePath!=null?typePath.in(allowedTypes):allowedTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
					userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds))
			));

		}
		else{
			if(user.allowAll()){
				allowAllTenantsWithoutDeny.addAll(securityContext.tenants());
			}
		}
		//role
		for (SecurityPermissionEntry<IRole> role : securityPermissions.rolePermissions()) {
			IRole roleEntity = role.entity();
			ISecurityTenant securityTenant = roleEntity.getTenant();
			if (!role.allowed().isEmpty()) {
				List<String> roleAllowedIds = role.allowed().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						idPath.in(roleAllowedIds),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied))
				));
			}
			if (specificRoleTypesPermissionRequired(role,userDenied,user)) {
				Set<String> roleAllowedTypes = role.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
				List<String> roleDeniedIds = role.denied().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						cb.equal(tenantIdPath, securityTenant.getId()),
						role.allowAll() ? cb.and() : typePath!=null?typePath.in(roleAllowedTypes):roleAllowedTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDeniedIds)),
						role.denied().isEmpty() ? cb.and() : cb.not(r.in(roleDeniedIds))

				));
			}
			else{
				if(role.allowAll()){
					allowAllTenantsWithoutDeny.add(securityTenant);
				}
			}

		}
		//tenant
		List<SecurityPermissionEntry<ISecurityTenant>> tenants = securityPermissions.tenantPermissions();
		List<String> tenantAllowed = tenants.stream().map(f -> f.allowed()).flatMap(f -> f.stream()).map(f->f.id()).collect(Collectors.toList());

		List<SecuredHolder> roleDenied = securityPermissions.rolePermissions().stream().map(f -> f.denied()).flatMap(f -> f.stream()).collect(Collectors.toList());

		List<String> roleDeniedIds = roleDenied.stream().map(f -> f.id()).toList();
		if (!tenantAllowed.isEmpty()) {
			securityPreds.add(cb.and(
					idPath.in(tenantAllowed),
					userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds)),
					roleDenied.isEmpty() ? cb.and() : cb.not(idPath.in(roleDeniedIds))

			));
		}
		for (SecurityPermissionEntry<ISecurityTenant> tenant : tenants) {
			if (specificTenantTypesPermissionRequired(tenant,userDenied,roleDenied)) {
				ISecurityTenant tenantEntity = tenant.entity();
				Set<String> allowedTenantTypes = tenant.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
				List<String> tenantDeniedIds = tenant.denied().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						cb.equal(tenantIdPath, tenantEntity.getId()),
						tenant.allowAll() ? cb.and() : typePath!=null?typePath.in(allowedTenantTypes):allowedTenantTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
						userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds)),
						roleDenied.isEmpty() ? cb.and() : cb.not(idPath.in(roleDeniedIds)),
						tenant.denied().isEmpty() ? cb.and() : cb.not(idPath.in(tenantDeniedIds))
				));
			}
			else{
				if(tenant.allowAll()){
					allowAllTenantsWithoutDeny.add(tenant.entity());
				}
			}
		}
		if(!allowAllTenantsWithoutDeny.isEmpty()){
			securityPreds.add(cb.or(tenantIdPath.in(allowAllTenantsWithoutDeny.stream().map(f->f.getId()).toList())));
		}


		predicates.add(cb.and(tenantIdPath.in(securityContext.tenants().stream().map(f->f.getId()).toList()),cb.or(securityPreds.toArray(new Predicate[0]))));

	}


	private static boolean specificTenantTypesPermissionRequired(SecurityPermissionEntry<ISecurityTenant> tenant, List<SecuredHolder> userDenied, List<SecuredHolder> roleDenied) {
		return !tenant.allowedTypes().isEmpty() && (!tenant.allowAll() || !userDenied.isEmpty() || !roleDenied.isEmpty() );
	}

	private static boolean specificUserTypePermissionRequired(SecurityPermissionEntry<ISecurityUser> user, List<SecuredHolder> userDenied) {
		return !user.allowedTypes().isEmpty()&& (!user.allowAll() || !userDenied.isEmpty());
	}

	private static boolean specificRoleTypesPermissionRequired(SecurityPermissionEntry<IRole> role, List<SecuredHolder> userDenied, SecurityPermissionEntry<ISecurityUser> user) {
		return !role.allowedTypes().isEmpty() && (!role.allowAll() || !userDenied.isEmpty() );
	}

	public boolean requiresSecurityPredicates(SecurityContext securityContext) {
		ISecurityUser user= securityContext.securityUser();
		if (user == null) {
			return false;
		}
		List<IRole> roles=securityContext.roles();
		return !isSuperAdmin(roles);
	}


	private boolean isSuperAdmin(List<IRole> roles) {
		for (IRole role : roles) {
			if (role.getId().equals("HzFnw-nVR0Olq6WBvwKcQg")) {
				return true;
			}

		}
		return false;

	}



}
