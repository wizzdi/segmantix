package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.model.IRole;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.wizzdi.segmantix.service.SecurityLinksExtractor.requiresSecurityPredicates;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificRoleTypesPermissionRequired;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificTenantTypesPermissionRequired;
import static com.wizzdi.segmantix.service.SecurityLinksExtractor.specificUserTypePermissionRequired;

public class CriteriaApiSecurityRepository {

	private static final Logger logger = LoggerFactory.getLogger(CriteriaApiSecurityRepository.class);

	private final String allTypesId;
	private final FieldPathProvider fieldPathProvider;
	private final SecurityLinksExtractor securityLinksExtractor;


	public CriteriaApiSecurityRepository(FieldPathProvider fieldPathProvider,
										 SecurityLinksExtractor securityLinksExtractor,
										 String allTypesId) {
		this.allTypesId=allTypesId;
		this.fieldPathProvider=fieldPathProvider;

        this.securityLinksExtractor = securityLinksExtractor;
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
		SecurityPermissions securityPermissions = securityLinksExtractor.getSecurityPermissions(securityContext,types);
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
						userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDenied)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(cb,r,user.deniedPermissionGroups(),fieldPathProvider,join))
				));
			}
			if (specificRoleTypesPermissionRequired(role,userDenied,user)) {
				Set<String> roleAllowedTypes = role.allowedTypes().stream().map(f -> f.type()).collect(Collectors.toSet());
				List<String> roleDeniedIds = role.denied().stream().map(f -> f.id()).toList();
				securityPreds.add(cb.and(
						cb.equal(tenantIdPath, tenant.getId()),
						role.allowAll() ? cb.and() : typePath!=null?typePath.in(roleAllowedTypes):roleAllowedTypes.contains(r.getJavaType().getCanonicalName())?cb.and():cb.or(),
						userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDeniedIds)),
						role.denied().isEmpty() ? cb.and() : cb.not(idPath.in(roleDeniedIds)),
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
						userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDenied)),
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
					userDenied.isEmpty() ? cb.and() : cb.not(idPath.in(userDenied)),
					roleDenied.isEmpty() ? cb.and() : cb.not(idPath.in(roleDenied))

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






}
