package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup_;

import com.wizzdi.segmantix.store.jpa.request.PermissionGroupFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class PermissionGroupRepository  implements SegmantixRepository {
	
	private final EntityManager em;
	private final SecuredBasicRepository securedBasicRepository;

	public PermissionGroupRepository(EntityManager em, SecuredBasicRepository securedBasicRepository) {
		this.em = em;
		this.securedBasicRepository = securedBasicRepository;
	}

	public List<PermissionGroup> listAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<PermissionGroup> q=cb.createQuery(PermissionGroup.class);
		Root<PermissionGroup> r=q.from(PermissionGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupPredicates(permissionGroupFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<PermissionGroup> query = em.createQuery(q);
		BasicRepository.addPagination(permissionGroupFilter,query);
		return query.getResultList();

	}

	public <T extends PermissionGroup> void addPermissionGroupPredicates(PermissionGroupFilter permissionGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(permissionGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(permissionGroupFilter.getExternalIds()!=null&&!permissionGroupFilter.getExternalIds().isEmpty()){
			predicates.add(r.get(PermissionGroup_.externalId).in(permissionGroupFilter.getExternalIds()));
		}
	}

	public long countAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<PermissionGroup> r=q.from(PermissionGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupPredicates(permissionGroupFilter,cb,q,r,predicates,securityContext);
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
