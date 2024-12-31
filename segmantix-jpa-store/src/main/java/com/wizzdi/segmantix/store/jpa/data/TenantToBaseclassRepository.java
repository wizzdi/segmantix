package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass_;
import com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
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



public class TenantToBaseclassRepository implements SegmantixRepository {
	
	private final EntityManager em;

	private final SecurityLinkRepository securityLinkRepository;

    public TenantToBaseclassRepository(EntityManager em, SecurityLinkRepository securityLinkRepository) {
        this.em = em;
        this.securityLinkRepository = securityLinkRepository;
    }

    public List<TenantToBaseclass> listAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<TenantToBaseclass> q=cb.createQuery(TenantToBaseclass.class);
		Root<TenantToBaseclass> r=q.from(TenantToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToBaseclassPredicates(tenantToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TenantToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(tenantToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends TenantToBaseclass> void addTenantToBaseclassPredicates(TenantToBaseclassFilter tenantToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(tenantToBaseclassFilter,cb,q,r,predicates,securityContext);
		if(tenantToBaseclassFilter.getTenants()!=null&&!tenantToBaseclassFilter.getTenants().isEmpty()){
			predicates.add(r.get(TenantToBaseclass_.tenant).in(tenantToBaseclassFilter.getTenants()));
		}
	}

	public long countAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<TenantToBaseclass> r=q.from(TenantToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToBaseclassPredicates(tenantToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securityLinkRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityLinkRepository.getByIdOrNull(id, c, securityContext);
	}
}
