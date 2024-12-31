package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass_;

import com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassFilter;
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



public class RoleToBaseclassRepository  implements SegmantixRepository {
	
	private final EntityManager em;

	public RoleToBaseclassRepository(EntityManager em, SecurityLinkRepository securityLinkRepository) {
		this.em = em;
		this.securityLinkRepository = securityLinkRepository;
	}

	private final SecurityLinkRepository securityLinkRepository;

	public List<RoleToBaseclass> listAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<RoleToBaseclass> q=cb.createQuery(RoleToBaseclass.class);
		Root<RoleToBaseclass> r=q.from(RoleToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToBaseclassPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<RoleToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(roleToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends RoleToBaseclass> void addRoleToBaseclassPredicates(RoleToBaseclassFilter roleToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
		if(roleToBaseclassFilter.getRoles()!=null&&!roleToBaseclassFilter.getRoles().isEmpty()){
			predicates.add(r.get(RoleToBaseclass_.role).in(roleToBaseclassFilter.getRoles()));
		}
	}

	public long countAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<RoleToBaseclass> r=q.from(RoleToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToBaseclassPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
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
