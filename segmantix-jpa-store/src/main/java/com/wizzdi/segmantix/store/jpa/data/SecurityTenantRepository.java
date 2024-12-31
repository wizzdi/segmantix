package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class SecurityTenantRepository implements SegmantixRepository {
	
	private final EntityManager em;
	private final BaseclassRepository baseclassRepository;
	private final SecurityEntityRepository securityEntityRepository;

    public SecurityTenantRepository(EntityManager em, BaseclassRepository baseclassRepository, SecurityEntityRepository securityEntityRepository) {
        this.em = em;
        this.baseclassRepository = baseclassRepository;
        this.securityEntityRepository = securityEntityRepository;
    }

    public List<SecurityTenant> listAllTenants(SecurityTenantFilter tenantFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityTenant> q=cb.createQuery(SecurityTenant.class);
		Root<SecurityTenant> r=q.from(SecurityTenant.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantPredicates(tenantFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(SecurityTenant_.name)));
		TypedQuery<SecurityTenant> query = em.createQuery(q);
		BaseclassRepository.addPagination(tenantFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityTenant> void addTenantPredicates(SecurityTenantFilter tenantFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityEntityRepository.addSecurityEntityPredicates(tenantFilter,cb,q,r,predicates,securityContext);
		if(tenantFilter.getUsers()!=null&&!tenantFilter.getUsers().isEmpty()){
			Join<T, TenantToUser> join=r.join(SecurityTenant_.tenantToUser);
			predicates.add(join.get(TenantToUser_.user).in(tenantFilter.getUsers()));
		}
	}

	public long countAllTenants(SecurityTenantFilter tenantFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityTenant> r=q.from(SecurityTenant.class);
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
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
