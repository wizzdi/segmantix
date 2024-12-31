package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass_;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



public class PermissionGroupToBaseclassRepository implements SegmantixRepository {
	
	private final EntityManager em;
	private final SecuredBasicRepository securedBasicRepository;

	public PermissionGroupToBaseclassRepository(EntityManager em, SecuredBasicRepository securedBasicRepository) {
		this.em = em;
		this.securedBasicRepository = securedBasicRepository;
	}

	public List<PermissionGroupToBaseclass> listAllPermissionGroupToBaseclasss(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<PermissionGroupToBaseclass> q=cb.createQuery(PermissionGroupToBaseclass.class);
		Root<PermissionGroupToBaseclass> r=q.from(PermissionGroupToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupToBaseclassPredicates(permissionGroupToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(getSorting(permissionGroupToBaseclassFilter, r, cb));
		TypedQuery<PermissionGroupToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(permissionGroupToBaseclassFilter,query);
		return query.getResultList();

	}

	private static Order getSorting(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, Root<PermissionGroupToBaseclass> r, CriteriaBuilder cb) {
		PermissionGroupToBaseclassFilter.SortBy sortBy = Optional.ofNullable(permissionGroupToBaseclassFilter.getSorting()).map(f->f.sortBy()).orElse(PermissionGroupToBaseclassFilter.SortBy.CLAZZ_NAME);
		boolean asc = Optional.ofNullable(permissionGroupToBaseclassFilter.getSorting()).map(f->f.asc()).orElse(true);
		Path<?> sortPath = switch (sortBy){
			case BASECLASS_ID -> r.get(PermissionGroupToBaseclass_.securedId);
			case BASECLASS_NAME -> r.get(PermissionGroupToBaseclass_.name);
			case BASECLASS_CREATION_DATE -> r.get(PermissionGroupToBaseclass_.securedCreationDate);
			default-> r.get(PermissionGroupToBaseclass_.securedType);
		};
        return asc ? cb.asc(sortPath) : cb.desc(sortPath);
	}

	public  <T extends PermissionGroupToBaseclass> void addPermissionGroupToBaseclassPredicates(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(permissionGroupToBaseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if (permissionGroupToBaseclassFilter.getSecuredIds() != null && !permissionGroupToBaseclassFilter.getSecuredIds().isEmpty()) {
			predicates.add(r.get(PermissionGroupToBaseclass_.securedId).in(permissionGroupToBaseclassFilter.getSecuredIds()));
		}
		if (permissionGroupToBaseclassFilter.getClazzes() != null && !permissionGroupToBaseclassFilter.getClazzes().isEmpty()) {
			Set<String> types=permissionGroupToBaseclassFilter.getClazzes().stream().map(f->f.name()).collect(Collectors.toSet());
			predicates.add(r.get(PermissionGroupToBaseclass_.securedType).in(types));
		}
		if (permissionGroupToBaseclassFilter.getPermissionGroups() != null && !permissionGroupToBaseclassFilter.getPermissionGroups().isEmpty()) {
			predicates.add(r.get(PermissionGroupToBaseclass_.permissionGroup).in(permissionGroupToBaseclassFilter.getPermissionGroups()));
		}
	}

	public long countAllPermissionGroupToBaseclasss(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<PermissionGroupToBaseclass> r=q.from(PermissionGroupToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupToBaseclassPredicates(permissionGroupToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securedBasicRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		securedBasicRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

}
