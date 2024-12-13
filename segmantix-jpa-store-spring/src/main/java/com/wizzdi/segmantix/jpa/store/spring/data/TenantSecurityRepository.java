package com.wizzdi.segmantix.jpa.store.spring.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantSecurity;
import com.flexicore.model.TenantSecurity_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityFilter;
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

public class TenantSecurityRepository  {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SecurityApiRepository securityApiRepository;

	public List<TenantSecurity> listAllTenantSecuritys(TenantSecurityFilter tenantSecurityFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<TenantSecurity> q=cb.createQuery(TenantSecurity.class);
		Root<TenantSecurity> r=q.from(TenantSecurity.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantSecurityPredicates(tenantSecurityFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TenantSecurity> query = em.createQuery(q);
		BasicRepository.addPagination(tenantSecurityFilter,query);
		return query.getResultList();

	}

	public <T extends TenantSecurity> void addTenantSecurityPredicates(TenantSecurityFilter tenantSecurityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityApiRepository.addSecurityPredicates(tenantSecurityFilter,cb,q,r,predicates,securityContext);
		if(tenantSecurityFilter.getTenants()!=null&&!tenantSecurityFilter.getTenants().isEmpty()){
			predicates.add(r.get(TenantSecurity_.tenant).in(tenantSecurityFilter.getTenants()));
		}
	}

	public long countAllTenantSecuritys(TenantSecurityFilter tenantSecurityFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<TenantSecurity> r=q.from(TenantSecurity.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantSecurityPredicates(tenantSecurityFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securityApiRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		securityApiRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return securityApiRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityApiRepository.getByIdOrNull(id, c, securityContext);
	}
}
