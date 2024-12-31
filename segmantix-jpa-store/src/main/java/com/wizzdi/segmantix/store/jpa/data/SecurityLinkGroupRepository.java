package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
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



public class SecurityLinkGroupRepository implements SegmantixRepository {
	
	private final EntityManager em;
	private final SecuredBasicRepository securedBasicRepository;
	private final SecurityLinkRepository securityLinkRepository;

    public SecurityLinkGroupRepository(EntityManager em, SecuredBasicRepository securedBasicRepository, SecurityLinkRepository securityLinkRepository) {
        this.em = em;
        this.securedBasicRepository = securedBasicRepository;
        this.securityLinkRepository = securityLinkRepository;
    }


    public List<SecurityLinkGroup> listAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityLinkGroup> q=cb.createQuery(SecurityLinkGroup.class);
		Root<SecurityLinkGroup> r=q.from(SecurityLinkGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		Join<SecurityLinkGroup, SecurityLink> join = addSecurityLinkGroupPredicates(securityLinkGroupFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).distinct(true);
		TypedQuery<SecurityLinkGroup> query = em.createQuery(q);
		BasicRepository.addPagination(securityLinkGroupFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityLinkGroup> Join<T,SecurityLink> addSecurityLinkGroupPredicates(SecurityLinkGroupFilter securityLinkGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityLinkGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityLinkGroupFilter.getSecurityLinkFilter()!=null){
			Join<T,SecurityLink> join=r.join(SecurityLinkGroup_.securityLinks);
			securityLinkRepository.addSecurityLinkPredicates(securityLinkGroupFilter.getSecurityLinkFilter(),cb,q,join,predicates,securityContext);
			predicates.add(cb.isFalse(join.get(SecurityLink_.softDelete)));
			return join;
		}
		return null;

	}

	public long countAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityLinkGroup> r=q.from(SecurityLinkGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkGroupPredicates(securityLinkGroupFilter,cb,q,r,predicates,securityContext);
		q.select(cb.countDistinct(r)).where(predicates.toArray(Predicate[]::new));
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
