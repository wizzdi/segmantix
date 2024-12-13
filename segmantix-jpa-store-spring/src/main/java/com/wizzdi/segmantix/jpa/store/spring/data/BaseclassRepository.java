package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.model.SecurityContext;
import com.flexicore.security.SecurityPermissionEntry;
import com.flexicore.security.SecurityPermissions;

import com.wizzdi.segmantix.jpa.store.spring.events.BasicCreated;
import com.wizzdi.segmantix.jpa.store.spring.events.BasicUpdated;
import com.wizzdi.segmantix.jpa.store.spring.request.BaseclassFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.SoftDeleteOption;
import com.wizzdi.segmantix.jpa.store.spring.service.OperationGroupLinkService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component

public class BaseclassRepository  {

	private static final Logger logger = LoggerFactory.getLogger(BaseclassRepository.class);
	@PersistenceContext
	private EntityManager em;

	@Autowired
	@Qualifier("allOps")
	@Lazy
	private Operation allOp;

	@Autowired
	@Lazy
	private OperationGroupLinkService operationGroupLinkService;

	@Autowired
	private BasicRepository basicRepository;
	@Autowired
	@Qualifier("dataAccessControlCache")
	private Cache dataAccessControlCache;

	@Autowired
	@Qualifier("operationToOperationGroupCache")
	private Cache operationToOperationGroupCache;

	public List<Baseclass> listAllBaseclass(BaseclassFilter baseclassFilter,SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Baseclass> q=cb.createQuery(Baseclass.class);
		Root<Baseclass> r=q.from(Baseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Baseclass_.name)));
		TypedQuery<Baseclass> query = em.createQuery(q);
		BasicRepository.addPagination(baseclassFilter,query);
		return query.getResultList();

	}

	private <T extends Baseclass> void addBaseclassPredicates(BaseclassFilter baseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		addBaseclassPredicates(baseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(baseclassFilter.getClazzes()!=null&&!baseclassFilter.getClazzes().isEmpty()){
			predicates.add(r.get(Baseclass_.clazz).in(baseclassFilter.getClazzes()));
		}
	}

	public long countAllBaseclass(BaseclassFilter baseclassFilter,SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Baseclass> r=q.from(Baseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	@EventListener
	public void invalidateUserCache(BasicCreated<UserSecurity> security) {
		Security link = security.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateRoleCache(BasicCreated<RoleSecurity> security) {
		Security link = security.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateTenantCache(BasicCreated<TenantSecurity> security) {
		Security link = security.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateUserCache(BasicUpdated<UserSecurity> security) {
		Security link = security.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateRoleCache(BasicUpdated<RoleSecurity> security) {
		Security link = security.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateTenantCache(BasicUpdated<TenantSecurity> security) {
		Security link = security.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateOperationGroupCache(BasicUpdated<OperationGroupLink> operationGroupLink){
		invalidateCache(operationGroupLink.getBaseclass());
	}

	@EventListener
	public void invalidateOperationGroupCache(BasicCreated<OperationGroupLink> operationGroupLink){
		invalidateCache(operationGroupLink.getBaseclass());
	}

	private void invalidateCache(OperationGroupLink operationGroupLink) {
		if(operationGroupLink.getOperation()==null){
			return;
		}
		operationToOperationGroupCache.evict(operationGroupLink.getOperation().getId());
	}


	private void invalidateCache(Security link) {
		if (link.getSecurityEntity() != null) {
			dataAccessControlCache.evict(link.getSecurityEntity().getId());
			logger.debug("evicted security entity " + link.getSecurityEntity().getId());
		}
	}

	public static <T> boolean addPagination(BaseclassFilter baseclassFilter, TypedQuery<T> q) {
		return BasicRepository.addPagination(baseclassFilter, q);
	}

	public <T extends Baseclass> void addBaseclassPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		if (basicPropertiesFilter != null) {
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter, cb, q, r, predicates);
		} else {
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT), cb, q, r, predicates);
		}
		if (securityContext != null) {
			addBaseclassPredicates(cb, q, r, predicates, securityContext);
		}
	}

	record SecurityHolder(List<UserSecurity> users, List<RoleSecurity> roles,
							  List<TenantSecurity> tenants) {
	}


	public SecurityPermissions getSecurityPermissions(SecurityContext securityContext) {
		SecurityHolder securityHolder = getSecurityHolder(securityContext);
		List<UserSecurity> userLinks = securityHolder.users();
		List<RoleSecurity> roleLinks = securityHolder.roles();
		List<TenantSecurity> tenantLinks = securityHolder.tenants();
		Map<String, Role> allRoles = securityContext.getAllRoles().stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Map<String, Tenant> allTenants = securityContext.getTenants().stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Set<String> relevantOps = securityContext.getOperation() != null ? Set.of(allOp.getId(), securityContext.getOperation().getId()) : null;
		Set<String> relevantOpGroups=securityContext.getOperation()!=null?getRelevantOpGroups(securityContext.getOperation()):null;
		List<UserSecurity> user = userLinks.stream().filter(f -> filterSecurityForOperation(f, relevantOps, relevantOpGroups)).toList();
		Map<String, List<RoleSecurity>> role = roleLinks.stream().filter(f ->filterSecurityForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getRole().getId()));
		Map<String, List<TenantSecurity>> tenant = tenantLinks.stream().filter(f -> filterSecurityForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getTenant().getId()));
		return new SecurityPermissions(SecurityPermissionEntry.of(securityContext.getUser(), user), role.entrySet().stream().map(f -> SecurityPermissionEntry.of(allRoles.get(f.getKey()), f.getValue())).toList(), tenant.entrySet().stream().map(f -> SecurityPermissionEntry.of(allTenants.get(f.getKey()), f.getValue())).toList());

	}

	private static boolean filterSecurityForOperation(Security f, Set<String> relevantOps, Set<String> relevantOpGroups) {
		return relevantOps == null || relevantOpGroups == null || (f.getOperation() != null && relevantOps.contains(f.getOperation().getId())) || (f.getOperationGroup() != null && relevantOpGroups.contains(f.getOperationGroup().getId()));
	}

	private Set<String> getRelevantOpGroups(Operation op) {
		return operationToOperationGroupCache.get(op.getId(), () -> operationGroupLinkService.listAllOperationGroupLinks(new OperationGroupLinkFilter().setOperations(Collections.singletonList(op)),null).stream().map(f->f.getOperationGroup().getId()).collect(Collectors.toSet()));
	}

	private SecurityHolder getSecurityHolder(SecurityContext securityContext) {
		List<UserSecurity> userPermissions = dataAccessControlCache.get(securityContext.getUser().getId(), List.class);
		List<List<RoleSecurity>> rolePermissions = securityContext.getAllRoles().stream().map(f -> (List<RoleSecurity>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		List<List<TenantSecurity>> tenantPermissions = securityContext.getTenants().stream().map(f -> (List<TenantSecurity>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		if (userPermissions != null && rolePermissions.stream().allMatch(f -> f != null) && tenantPermissions.stream().allMatch(f -> f != null)) {
			List<RoleSecurity> roleLinks = rolePermissions.stream().flatMap(f -> f.stream()).toList();
			List<TenantSecurity> tenantLinks = tenantPermissions.stream().flatMap(f -> f.stream()).toList();
			logger.debug("cache hit users: {} , roles: {} , tenants: {}", userPermissions.size(), roleLinks.size(), tenantLinks.size());
			return new SecurityHolder(userPermissions, roleLinks, tenantLinks);

		}
		List<Security> securitys = getSecuritys(securityContext);
		List<UserSecurity> userLinks = securitys.stream().filter(f -> f instanceof UserSecurity).map(f -> (UserSecurity) f).toList();
		List<RoleSecurity> roleLinks = securitys.stream().filter(f -> f instanceof RoleSecurity).map(f -> (RoleSecurity) f).toList();
		List<TenantSecurity> tenantLinks = securitys.stream().filter(f -> f instanceof TenantSecurity).map(f -> (TenantSecurity) f).toList();
		dataAccessControlCache.put(securityContext.getUser().getId(), userLinks);
		for (Role role : securityContext.getAllRoles()) {
			dataAccessControlCache.put(role.getId(), roleLinks.stream().filter(f -> f.getRole().getId().equals(role.getId())).toList());
		}
		for (Tenant tenant : securityContext.getTenants()) {
			dataAccessControlCache.put(tenant.getId(), tenantLinks.stream().filter(f -> f.getTenant().getId().equals(tenant.getId())).toList());
		}
		return new SecurityHolder(userLinks, roleLinks, tenantLinks);

	}

	public List<Security> getSecuritys(SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Security> q = cb.createQuery(Security.class);
		Root<Security> r = q.from(Security.class);
		Root<UserSecurity> user = cb.treat(r, UserSecurity.class);
		Root<RoleSecurity> role = cb.treat(r, RoleSecurity.class);
		Root<TenantSecurity> tenant = cb.treat(r, TenantSecurity.class);
		Map<String, List<Role>> roleMap = securityContext.getRoleMap();
		List<Role> roles = roleMap.values()
				.stream()
				.flatMap(List::stream).toList();
		q.select(r).where(
				cb.and(
						cb.isFalse(r.get(Security_.softDelete)),
						cb.or(
								user.get(UserSecurity_.user).in(securityContext.getUser()),
								roles.isEmpty()?cb.or():role.get(RoleSecurity_.role).in(roles),
								securityContext.getTenants().isEmpty()?cb.or():tenant.get(TenantSecurity_.tenant).in(securityContext.getTenants())
						)));
		return em.createQuery(q).getResultList();
	}

	public static <T extends Baseclass> Predicate instanceGroupPredicate(From<?, T> r, List<InstanceGroup> denied, AtomicReference<Join<T, InstanceGroupLink>> atomicReference){
		if(atomicReference.get()==null){
			atomicReference.set(r.join(Baseclass_.instanceGroupLinkes,JoinType.LEFT));
		}
		Join<T, InstanceGroupLink> join = atomicReference.get();
		return join.get(InstanceGroupLink_.instanceGroup).in(denied);
	}
	public <T extends Baseclass> void addBaseclassPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		if (!requiresSecurityPredicates(securityContext)) {
			return;
		}

		SecurityPermissions securityPermissions = getSecurityPermissions(securityContext);
		List<Predicate> securityPreds = new ArrayList<>();
		AtomicReference<Join<T, InstanceGroupLink>> join = new AtomicReference<>(null);
		securityPreds.add(cb.equal(r.get(Baseclass_.creator), securityContext.getUser()));//creator
		//user
		SecurityPermissionEntry<User> user = securityPermissions.userPermissions();
		if (!user.allowed().isEmpty()) {
			securityPreds.add(r.in(user.allowed()));
		}
		List<Baseclass> userDenied = user.denied();
		List<Tenant> allowAllTenantsWithoutDeny=new ArrayList<>(); // in the case of allow all tenants ,and no denies we can improve the query or avoid a bunch of ors and use an IN clause
		if (specificUserTypePermissionRequired(user,userDenied)) {
			securityPreds.add(cb.and(
					r.get(Baseclass_.tenant).in(securityContext.getTenants()),
					user.allowAll() ? cb.and() : r.get(Baseclass_.clazz).in(user.allowedTypes()),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					user.deniedInstanceGroups().isEmpty()?cb.and(): cb.not(instanceGroupPredicate(r,user.deniedInstanceGroups(),join))
			));

		}
		else{
			if(user.allowAll()){
				allowAllTenantsWithoutDeny.addAll(securityContext.getTenants());
			}
		}
		if (!user.allowedInstanceGroups().isEmpty()) {
			securityPreds.add(instanceGroupPredicate(r,user.allowedInstanceGroups(),join));
		}
		//role
		for (SecurityPermissionEntry<Role> role : securityPermissions.rolePermissions()) {
			Role roleEntity = role.entity();
			Tenant tenant = roleEntity.getSecurity().getTenant();
			if (!role.allowed().isEmpty()) {
				securityPreds.add(cb.and(
						r.in(role.allowed()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						user.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,user.deniedInstanceGroups(),join))
				));
			}
			if (specificRoleTypesPermissionRequired(role,userDenied,user)) {
				securityPreds.add(cb.and(
						cb.equal(r.get(Baseclass_.tenant), tenant),
						role.allowAll() ? cb.and() : r.get(Baseclass_.clazz).in(role.allowedTypes()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						role.denied().isEmpty() ? cb.and() : cb.not(r.in(role.denied())),
						user.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,user.deniedInstanceGroups(),join)),
						role.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,role.deniedInstanceGroups(),join))

				));
			}
			else{
				if(role.allowAll()){
					allowAllTenantsWithoutDeny.add(tenant);
				}
			}
			if (!role.allowedInstanceGroups().isEmpty()) {
				securityPreds.add(cb.and(
						instanceGroupPredicate(r,role.allowedInstanceGroups(),join),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						user.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,user.deniedInstanceGroups(),join))

				));
			}

		}
		//tenant
		List<SecurityPermissionEntry<Tenant>> tenants = securityPermissions.tenantPermissions();
		List<InstanceGroup> tenantAllowedInstanceGroups = tenants.stream().map(f -> f.allowedInstanceGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());
		List<Baseclass> tenantAllowed = tenants.stream().map(f -> f.allowed()).flatMap(f -> f.stream()).collect(Collectors.toList());

		List<Baseclass> roleDenied = securityPermissions.rolePermissions().stream().map(f -> f.denied()).flatMap(f -> f.stream()).collect(Collectors.toList());
		List<InstanceGroup> roleInstanceGroupDenied = securityPermissions.rolePermissions().stream().map(f -> f.deniedInstanceGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());

		if (!tenantAllowed.isEmpty()) {
			securityPreds.add(cb.and(
					r.in(tenantAllowed),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied)),
					user.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,user.deniedInstanceGroups(),join)),
					roleInstanceGroupDenied.isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,roleInstanceGroupDenied,join))

			));
		}
		if (!tenantAllowedInstanceGroups.isEmpty()) {
			securityPreds.add(cb.and(
					instanceGroupPredicate(r,tenantAllowedInstanceGroups,join),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied))

			));
		}
		for (SecurityPermissionEntry<Tenant> tenant : tenants) {
			if (specificTenantTypesPermissionRequired(tenant,userDenied,roleDenied,user,roleInstanceGroupDenied)) {
				Tenant tenantEntity = tenant.entity();
				securityPreds.add(cb.and(
						cb.equal(r.get(Baseclass_.tenant), tenantEntity),

						tenant.allowAll() ? cb.and() : r.get(Baseclass_.clazz).in(tenant.allowedTypes()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied)),
						tenant.denied().isEmpty() ? cb.and() : cb.not(r.in(tenant.denied())),
						user.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,user.deniedInstanceGroups(),join)),
						roleInstanceGroupDenied.isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,roleInstanceGroupDenied,join)),
						tenant.deniedInstanceGroups().isEmpty()?cb.and():cb.not(instanceGroupPredicate(r,tenant.deniedInstanceGroups(),join))
				));
			}
			else{
				if(tenant.allowAll()){
					allowAllTenantsWithoutDeny.add(tenant.entity());
				}
			}
		}
		if(!allowAllTenantsWithoutDeny.isEmpty()){
			securityPreds.add(cb.or(r.get(Baseclass_.tenant).in(allowAllTenantsWithoutDeny)));
		}


		predicates.add(cb.and(r.get(Baseclass_.tenant).in(securityContext.getTenants()),cb.or(securityPreds.toArray(new Predicate[0]))));

	}

	private static boolean specificTenantTypesPermissionRequired(SecurityPermissionEntry<Tenant> tenant, List<Baseclass> userDenied, List<Baseclass> roleDenied, SecurityPermissionEntry<User> user, List<InstanceGroup> roleInstanceGroupDenied) {
		return !tenant.allowedTypes().isEmpty() && (!tenant.allowAll() || !userDenied.isEmpty() || !roleDenied.isEmpty() || !user.deniedInstanceGroups().isEmpty() || !roleInstanceGroupDenied.isEmpty());
	}

	private static boolean specificUserTypePermissionRequired(SecurityPermissionEntry<User> user, List<Baseclass> userDenied) {
		return !user.allowedTypes().isEmpty()&& (!user.allowAll() || !userDenied.isEmpty() || !user.deniedInstanceGroups().isEmpty());
	}

	private static boolean specificRoleTypesPermissionRequired(SecurityPermissionEntry<Role> role, List<Baseclass> userDenied, SecurityPermissionEntry<User> user) {
		return !role.allowedTypes().isEmpty() && (!role.allowAll() || !userDenied.isEmpty() || !user.deniedInstanceGroups().isEmpty());
	}

	public boolean requiresSecurityPredicates(SecurityContext securityContext) {
		if (securityContext == null) {
			return false;
		}
		Map<String, List<Role>> roles = securityContext.getRoleMap();
		List<Role> allRoles = roles.values().stream().flatMap(f -> f.stream()).toList();
		return !isSuperAdmin(allRoles);
	}


	private boolean isSuperAdmin(List<Role> roles) {
		for (Role role : roles) {
			if (role.getId().equals("HzFnw-nVR0Olq6WBvwKcQg")) {
				return true;
			}

		}
		return false;

	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Baseclass_.id).in(ids));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Baseclass_.id), id));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Basic_.id), id));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}


	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return basicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return basicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return basicRepository.findByIdOrNull(type, id);
	}

	public <T> T merge(T base) {
		return basicRepository.merge(base);
	}

	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return basicRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		basicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}


	public <T> T merge(T base, boolean updateDate) {
		return basicRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		basicRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		basicRepository.massMerge(toMerge, updatedate);
	}
}
