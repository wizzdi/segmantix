package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.TenantToUser;
import com.wizzdi.segmantix.impl.model.TenantToUser_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component

public class TenantToUserRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;



	public List<TenantToUser> listAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<TenantToUser> q=cb.createQuery(TenantToUser.class);
		Root<TenantToUser> r=q.from(TenantToUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToUserPredicates(tenantToUserFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TenantToUser> query = em.createQuery(q);
		BasicRepository.addPagination(tenantToUserFilter,query);
		return query.getResultList();

	}

	public <T extends TenantToUser> void addTenantToUserPredicates(TenantToUserFilter tenantToUserFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(tenantToUserFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(tenantToUserFilter.getTenants()!=null&&!tenantToUserFilter.getTenants().isEmpty()){
			predicates.add(r.get(TenantToUser_.tenant).in(tenantToUserFilter.getTenants()));
		}

		if(tenantToUserFilter.getUsers()!=null&&!tenantToUserFilter.getUsers().isEmpty()){
			predicates.add(r.get(TenantToUser_.user).in(tenantToUserFilter.getUsers()));

		}
	}

	public long countAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<TenantToUser> r=q.from(TenantToUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToUserPredicates(tenantToUserFilter,cb,q,r,predicates,securityContext);
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}
}
