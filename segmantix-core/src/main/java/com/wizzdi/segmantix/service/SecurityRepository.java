package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IRoleSecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.ITenantSecurityLink;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.model.IUserSecurityLink;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.internal.SecuredHolder;
import com.wizzdi.segmantix.internal.SecurityPermissionEntry;
import com.wizzdi.segmantix.internal.SecurityPermissions;
import jakarta.persistence.EntityManager;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SecurityRepository {

	private static final Logger logger = LoggerFactory.getLogger(SecurityRepository.class);

	private final IOperation allOp;
	private final String allTypesId;
	private final OperationGroupLinkProvider operationGroupLinkProvider;
	private final SecurityLinkProvider securityProvider;
	private final FieldPathProvider fieldPathProvider;
	private final Cache dataAccessControlCache;
	private final Cache operationToOperationGroupCache;

	public SecurityRepository(FieldPathProvider fieldPathProvider, OperationGroupLinkProvider operationGroupLinkProvider, SecurityLinkProvider securityProvider,
							  Cache dataAccessControlCache, Cache operationToOperationGroupCache, IOperation allOp,String allTypesId) {
		this.allOp = allOp;
		this.allTypesId=allTypesId;
		this.fieldPathProvider=fieldPathProvider;
		this.operationGroupLinkProvider = operationGroupLinkProvider;
		this.securityProvider = securityProvider;
		this.dataAccessControlCache = dataAccessControlCache;
		this.operationToOperationGroupCache = operationToOperationGroupCache;
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

	public static <T> Predicate permissionGroupPredicate(CriteriaBuilder cb,From<?, T> r, List<IInstanceGroup> instanceGroups,FieldPathProvider fieldPathProvider, AtomicReference<Path<String>> instanceGroupPath){
		if(instanceGroupPath.get()==null){
			instanceGroupPath.set(fieldPathProvider.getInstanceGroupPath(r, cb));
		}
		Set<String> instanceGroupIds = instanceGroups.stream().map(f -> f.getId()).collect(Collectors.toSet());
		return instanceGroupPath.get().in(instanceGroupIds);
	}


	public <T > void addSecurityPredicates(EntityManager em,CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, ISecurityContext securityContext) {
		if (!requiresSecurityPredicates(securityContext)) {
			return;
		}
		Set<String> types=getRelevantTypes(em,r);
		types.add(allTypesId);
		SecurityPermissions securityPermissions = getSecurityPermissions(securityContext,types);
		List<Predicate> securityPreds = new ArrayList<>();
		AtomicReference<Path<String>> join = new AtomicReference<>(null);
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
					userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds)),
					user.deniedPermissionGroups().isEmpty()?cb.and(): cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join))
			));

		}
		else{
			if(user.allowAll()){
				allowAllTenantsWithoutDeny.addAll(securityContext.tenants());
			}
		}
		if (!user.allowedPermissionGroups().isEmpty()) {
			securityPreds.add(permissionGroupPredicate(cb,r,user.allowedPermissionGroups(),fieldPathProvider,join));
		}
		//role
		for (SecurityPermissionEntry<IRole> role : securityPermissions.rolePermissions()) {
			IRole roleEntity = role.entity();
			ITenant tenant = roleEntity.getTenant();
			if (!role.allowed().isEmpty()) {
				List<String> roleAllowedIds = role.allowed().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						idPath.in(roleAllowedIds),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join))
				));
			}
			if (specificRoleTypesPermissionRequired(role,userDenied,user)) {
				Set<String> roleAllowedTypes = role.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
				List<String> roleDeniedIds = role.denied().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						cb.equal(tenantIdPath, tenant.getId()),
						role.allowAll() ? cb.and() : typePath!=null?typePath.in(roleAllowedTypes):roleAllowedTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDeniedIds)),
						role.denied().isEmpty() ? cb.and() : cb.not(r.in(roleDeniedIds)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join)),
						role.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,role.deniedPermissionGroups(),fieldPathProvider,join))



				));
			}
			else{
				if(role.allowAll()){
					allowAllTenantsWithoutDeny.add(tenant);
				}
			}
			if (!role.allowedPermissionGroups().isEmpty()) {
				securityPreds.add(cb.and(
						permissionGroupPredicate(cb,r,role.allowedPermissionGroups(),fieldPathProvider,join),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join))

				));
			}

		}
		//tenant
		List<SecurityPermissionEntry<ITenant>> tenants = securityPermissions.tenantPermissions();

		List<IInstanceGroup> tenantAllowedPermissionGroups = tenants.stream().map(f -> f.allowedPermissionGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());
		List<String> tenantAllowed = tenants.stream().map(f -> f.allowed()).flatMap(f -> f.stream()).map(f->f.id()).collect(Collectors.toList());

		List<SecuredHolder> roleDenied = securityPermissions.rolePermissions().stream().map(f -> f.denied()).flatMap(f -> f.stream()).collect(Collectors.toList());

		List<String> roleDeniedIds = roleDenied.stream().map(f -> f.id()).toList();

		List<IInstanceGroup> rolePermissionGroupDenied = securityPermissions.rolePermissions().stream().map(f -> f.deniedPermissionGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());
		if (!tenantAllowed.isEmpty()) {
			securityPreds.add(cb.and(
					idPath.in(tenantAllowed),
					userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds)),
					roleDenied.isEmpty() ? cb.and() : cb.not(idPath.in(roleDeniedIds)),
					user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join)),
					rolePermissionGroupDenied.isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,rolePermissionGroupDenied,fieldPathProvider,join))


			));
		}
		if (!tenantAllowedPermissionGroups.isEmpty()) {
			securityPreds.add(cb.and(
					permissionGroupPredicate(cb,r,tenantAllowedPermissionGroups,fieldPathProvider,join),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied))

			));
		}
		for (SecurityPermissionEntry<ITenant> tenant : tenants) {
			if (specificTenantTypesPermissionRequired(tenant,userDenied,roleDenied,user,rolePermissionGroupDenied)) {
				ITenant tenantEntity = tenant.entity();
				Set<String> allowedTenantTypes = tenant.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
				List<String> tenantDeniedIds = tenant.denied().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						cb.equal(tenantIdPath, tenantEntity.getId()),
						tenant.allowAll() ? cb.and() : typePath!=null?typePath.in(allowedTenantTypes):allowedTenantTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
						userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds)),
						roleDenied.isEmpty() ? cb.and() : cb.not(idPath.in(roleDeniedIds)),
						tenant.denied().isEmpty() ? cb.and() : cb.not(idPath.in(tenantDeniedIds)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join)),
						rolePermissionGroupDenied.isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,rolePermissionGroupDenied,fieldPathProvider,join)),
						tenant.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,tenant.deniedPermissionGroups(),fieldPathProvider,join))

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

	private <T> Set<String> getRelevantTypes(EntityManager em, From<?, T> r) {
		Class<T> bindableJavaType = r.getModel().getBindableJavaType();
		return em.getMetamodel().getEntities().stream().map(f->f.getJavaType()).filter(f->bindableJavaType.isAssignableFrom(f)).map(f->f.getSimpleName()).collect(Collectors.toSet());
	}


	private static boolean specificTenantTypesPermissionRequired(SecurityPermissionEntry<ITenant> tenant, List<SecuredHolder> userDenied, List<SecuredHolder> roleDenied , SecurityPermissionEntry<IUser> user, List<IInstanceGroup> rolePermissionGroupDenied) {
		return !tenant.allowedTypes().isEmpty() && (!tenant.allowAll() || !userDenied.isEmpty() || !roleDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty() || !rolePermissionGroupDenied.isEmpty());
	}

	private static boolean specificUserTypePermissionRequired(SecurityPermissionEntry<IUser> user, List<SecuredHolder> userDenied) {
		return !user.allowedTypes().isEmpty()&& (!user.allowAll() || !userDenied.isEmpty()|| !user.deniedPermissionGroups().isEmpty());
	}

	private static boolean specificRoleTypesPermissionRequired(SecurityPermissionEntry<IRole> role, List<SecuredHolder> userDenied, SecurityPermissionEntry<IUser> user) {
		return !role.allowedTypes().isEmpty() && (!role.allowAll() || !userDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty());
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
