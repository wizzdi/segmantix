package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.TenantFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component

public class TenantRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private SecurityEntityRepository securityEntityRepository;

	public List<Tenant> listAllTenants(TenantFilter tenantFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Tenant> q=cb.createQuery(Tenant.class);
		Root<Tenant> r=q.from(Tenant.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantPredicates(tenantFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Tenant_.name)));
		TypedQuery<Tenant> query = em.createQuery(q);
		BaseclassRepository.addPagination(tenantFilter,query);
		return query.getResultList();

	}

	public <T extends Tenant> void addTenantPredicates(TenantFilter tenantFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityEntityRepository.addSecurityEntityPredicates(tenantFilter,cb,q,r,predicates,securityContext);
		if(tenantFilter.getUsers()!=null&&!tenantFilter.getUsers().isEmpty()){
			Join<T, TenantToUser> join=r.join(Tenant_.tenantToUser);
			predicates.add(join.get(TenantToUser_.user).in(tenantFilter.getUsers()));
		}
	}

	public long countAllTenants(TenantFilter tenantFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Tenant> r=q.from(Tenant.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantPredicates(tenantFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return baseclassRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		baseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return baseclassRepository.findByIdOrNull(type, id);
	}
}
