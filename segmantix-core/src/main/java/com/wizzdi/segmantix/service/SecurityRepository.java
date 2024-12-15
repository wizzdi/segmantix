package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IInstanceGroupLink;
import com.wizzdi.segmantix.api.model.IRoleSecurity;
import com.wizzdi.segmantix.api.model.ISecurity;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.ITenantSecurity;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.model.IUserSecurity;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.api.service.InstanceGroupLinkProvider;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.internal.SecuredHolder;
import com.wizzdi.segmantix.internal.SecurityPermissionEntry;
import com.wizzdi.segmantix.internal.SecurityPermissions;
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

public class SecurityRepository {

	private static final Logger logger = LoggerFactory.getLogger(SecurityRepository.class);

	private final IOperation allOp;
	private final OperationGroupLinkProvider operationGroupLinkProvider;
	private final SecurityLinkProvider securityProvider;
	private final InstanceGroupLinkProvider instanceGroupLinkProvider;
	private final FieldPathProvider fieldPathProvider;
	private final Cache dataAccessControlCache;
	private final Cache operationToOperationGroupCache;

	public SecurityRepository(FieldPathProvider fieldPathProvider, OperationGroupLinkProvider operationGroupLinkProvider, SecurityLinkProvider securityProvider, InstanceGroupLinkProvider instanceGroupLinkProvider,
							  Cache dataAccessControlCache, Cache operationToOperationGroupCache, IOperation allOp) {
		this.allOp = allOp;
		this.fieldPathProvider=fieldPathProvider;
		this.operationGroupLinkProvider = operationGroupLinkProvider;
		this.securityProvider = securityProvider;
		this.instanceGroupLinkProvider = instanceGroupLinkProvider;
		this.dataAccessControlCache = dataAccessControlCache;
		this.operationToOperationGroupCache = operationToOperationGroupCache;
	}

	record SecurityHolder(List<IUserSecurity> users, List<IRoleSecurity> roles,
							  List<ITenantSecurity> tenants,
							  Map<String, List<IInstanceGroupLink>> instanceGroupLinkes) {
	}


	public SecurityPermissions getSecurityPermissions(ISecurityContext securityContext) {
		IUser user=securityContext.user();
		List<? extends IRole> roles = securityContext.roles();
		List<? extends ITenant> tenants=securityContext.tenants();
		IOperation operation=securityContext.operation();
		SecurityHolder securityHolder = getSecurityHolder(securityContext);
		List<IUserSecurity> userLinks = securityHolder.users();
		List<IRoleSecurity> roleLinks = securityHolder.roles();
		List<ITenantSecurity> tenantLinks = securityHolder.tenants();
		Map<String, List<IInstanceGroupLink>> instanceGroupLinkes = securityHolder.instanceGroupLinkes();
		Map<String, IRole> allRoles = roles.stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Map<String, ITenant> allTenants = tenants.stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Set<String> relevantOps = operation!= null ? Set.of(allOp.getId(), operation.getId()) : null;
		Set<String> relevantOpGroups=operation!=null?getRelevantOpGroups(operation):null;
		List<IUserSecurity> userLinksForOp = userLinks.stream().filter(f -> filterSecurityForOperation(f, relevantOps, relevantOpGroups)).toList();
		Map<String, List<IRoleSecurity>> role = roleLinks.stream().filter(f ->filterSecurityForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getRole().getId()));
		Map<String, List<ITenantSecurity>> tenant = tenantLinks.stream().filter(f -> filterSecurityForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getTenant().getId()));
		return new SecurityPermissions(SecurityPermissionEntry.of(user, userLinksForOp,instanceGroupLinkes), role.entrySet().stream().map(f -> SecurityPermissionEntry.of(allRoles.get(f.getKey()), f.getValue(), instanceGroupLinkes)).toList(), tenant.entrySet().stream().map(f -> SecurityPermissionEntry.of(allTenants.get(f.getKey()), f.getValue(), instanceGroupLinkes)).toList());

	}

	private static boolean filterSecurityForOperation(ISecurity f, Set<String> relevantOps, Set<String> relevantOpGroups) {
		return relevantOps == null || relevantOpGroups == null || (f.getOperation() != null && relevantOps.contains(f.getOperation().getId())) || (f.getOperationGroup() != null && relevantOpGroups.contains(f.getOperationGroup().getId()));
	}

	private Set<String> getRelevantOpGroups(IOperation op) {
		return operationToOperationGroupCache.get(op.getId(), () -> operationGroupLinkProvider.listAllOperationGroupLinks(Collections.singletonList(op)).stream().map(f->f.getOperationGroup().getId()).collect(Collectors.toSet()));
	}

	private SecurityHolder getSecurityHolder(ISecurityContext securityContext) {
		IUser user=securityContext.user();
		List<? extends IRole> roles=securityContext.roles();
		List<? extends ITenant> tenants=securityContext.tenants();
		List<IUserSecurity> userPermissions = dataAccessControlCache.get(user.getId(), List.class);
		List<List<IRoleSecurity>> rolePermissions = roles.stream().map(f -> (List<IRoleSecurity>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		List<List<ITenantSecurity>> tenantPermissions = tenants.stream().map(f -> (List<ITenantSecurity>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		List<IInstanceGroup> cachedInstanceGroups=extractUsedInstanceGroups(userPermissions,rolePermissions,tenantPermissions);
		List<List<IInstanceGroupLink>> instanceGroupLinkCache = cachedInstanceGroups.stream().map(f -> (List<IInstanceGroupLink>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		if (userPermissions != null && rolePermissions.stream().allMatch(f -> f != null) && tenantPermissions.stream().allMatch(f -> f != null)&&instanceGroupLinkCache.stream().allMatch(f->f!=null)) {
			List<IRoleSecurity> roleLinks = rolePermissions.stream().flatMap(f -> f.stream()).toList();
			List<ITenantSecurity> tenantLinks = tenantPermissions.stream().flatMap(f -> f.stream()).toList();
			Map<String,List<IInstanceGroupLink>> instanceGroupLinkes=instanceGroupLinkCache.stream().flatMap(List::stream).collect(Collectors.groupingBy(f->f.getInstanceGroup().getId()));
			logger.debug("cache hit users: {} , roles: {} , tenants: {} , permission group links: {}", userPermissions.size(), roleLinks.size(), tenantLinks.size(),instanceGroupLinkCache.size());
			return new SecurityHolder(userPermissions, roleLinks, tenantLinks, instanceGroupLinkes);

		}
		List<ISecurity> securitys = this.securityProvider.getSecurityLinks(securityContext);
		List<IInstanceGroup> instanceGroups=securitys.stream().map(f->f.getInstanceGroup()).filter(f->f!=null).toList();
		Map<String,List<IInstanceGroupLink>> instanceGroupLinkes=instanceGroups.isEmpty()?Collections.emptyMap():this.instanceGroupLinkProvider.getInstanceGroupLinks(instanceGroups).stream().collect(Collectors.groupingBy(f->f.getInstanceGroup().getId()));
		List<IUserSecurity> userLinks = securitys.stream().filter(f -> f instanceof IUserSecurity).map(f -> (IUserSecurity) f).toList();
		List<IRoleSecurity> roleLinks = securitys.stream().filter(f -> f instanceof IRoleSecurity).map(f -> (IRoleSecurity) f).toList();
		List<ITenantSecurity> tenantLinks = securitys.stream().filter(f -> f instanceof ITenantSecurity).map(f -> (ITenantSecurity) f).toList();
		dataAccessControlCache.put(user.getId(), userLinks);
		for (IRole role : roles) {
			dataAccessControlCache.put(role.getId(), roleLinks.stream().filter(f -> f.getRole().getId().equals(role.getId())).toList());
		}
		for (ITenant tenant : tenants) {
			dataAccessControlCache.put(tenant.getId(), tenantLinks.stream().filter(f -> f.getTenant().getId().equals(tenant.getId())).toList());
		}
		for (IInstanceGroup instanceGroup : instanceGroups) {
			List<IInstanceGroupLink> links=instanceGroupLinkes.getOrDefault(instanceGroup.getId(),Collections.emptyList());
			dataAccessControlCache.put(instanceGroup.getId(), links);
		}
		return new SecurityHolder(userLinks, roleLinks, tenantLinks,instanceGroupLinkes);

	}

	private List<IInstanceGroup> extractUsedInstanceGroups(List<IUserSecurity> userPermissions, List<List<IRoleSecurity>> rolePermissions, List<List<ITenantSecurity>> tenantPermissions) {
		List<IInstanceGroup> toRet = Optional.ofNullable(userPermissions).stream().flatMap(List::stream).map(f -> f.getInstanceGroup()).filter(f->f!=null).collect(Collectors.toList());
		List<IInstanceGroup> role = rolePermissions.stream().filter(f->f!=null).flatMap(List::stream).map(f -> f.getInstanceGroup()).filter(f -> f != null).toList();
		List<IInstanceGroup> tenant = tenantPermissions.stream().filter(f->f!=null).flatMap(List::stream).map(f -> f.getInstanceGroup()).filter(f -> f != null).toList();
		toRet.addAll(role);
		toRet.addAll(tenant);
		return toRet;
	}


	public <T > void addSecurityPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, ISecurityContext securityContext) {
		if (!requiresSecurityPredicates(securityContext)) {
			return;
		}

		SecurityPermissions securityPermissions = getSecurityPermissions(securityContext);
		List<Predicate> securityPreds = new ArrayList<>();
		Path<String> creatorIdPath = fieldPathProvider.getCreatorIdPath(r);
		securityPreds.add(cb.equal(creatorIdPath, securityContext.user().getId()));//creator
		//user
		SecurityPermissionEntry<IUser> user = securityPermissions.userPermissions();
		Path<String> idPath = fieldPathProvider.getSecurityId(r);
		if (!user.allowed().isEmpty()) {
			securityPreds.add(idPath.in(user.allowed().stream().map(f->f.id()).toList()));
		}
		List<SecuredHolder> userDenied = user.denied();
		List<ITenant> allowAllTenantsWithoutDeny=new ArrayList<>(); // in the case of allow all tenants ,and no denies we can improve the query or avoid a bunch of ors and use an IN clause
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
			ITenant tenant = roleEntity.getTenant();
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
						cb.equal(tenantIdPath, tenant.getId()),
						role.allowAll() ? cb.and() : typePath!=null?typePath.in(roleAllowedTypes):roleAllowedTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDeniedIds)),
						role.denied().isEmpty() ? cb.and() : cb.not(r.in(roleDeniedIds))

				));
			}
			else{
				if(role.allowAll()){
					allowAllTenantsWithoutDeny.add(tenant);
				}
			}

		}
		//tenant
		List<SecurityPermissionEntry<ITenant>> tenants = securityPermissions.tenantPermissions();
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
		for (SecurityPermissionEntry<ITenant> tenant : tenants) {
			if (specificTenantTypesPermissionRequired(tenant,userDenied,roleDenied)) {
				ITenant tenantEntity = tenant.entity();
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


	private static boolean specificTenantTypesPermissionRequired(SecurityPermissionEntry<ITenant> tenant, List<SecuredHolder> userDenied, List<SecuredHolder> roleDenied) {
		return !tenant.allowedTypes().isEmpty() && (!tenant.allowAll() || !userDenied.isEmpty() || !roleDenied.isEmpty() );
	}

	private static boolean specificUserTypePermissionRequired(SecurityPermissionEntry<IUser> user, List<SecuredHolder> userDenied) {
		return !user.allowedTypes().isEmpty()&& (!user.allowAll() || !userDenied.isEmpty());
	}

	private static boolean specificRoleTypesPermissionRequired(SecurityPermissionEntry<IRole> role, List<SecuredHolder> userDenied, SecurityPermissionEntry<IUser> user) {
		return !role.allowedTypes().isEmpty() && (!role.allowAll() || !userDenied.isEmpty() );
	}

	public boolean requiresSecurityPredicates(ISecurityContext securityContext) {
		if(securityContext==null){
			return false;
		}
		IUser user= securityContext.user();
		if (user == null) {
			return false;
		}
		List<? extends IRole> roles=securityContext.roles();
		return !isSuperAdmin(roles);
	}


	private boolean isSuperAdmin(List<? extends IRole> roles) {
		return roles.stream().anyMatch(f->f.isSuperAdmin());
	}



}
