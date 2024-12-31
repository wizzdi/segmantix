package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.store.jpa.request.SecurityUserFilter;
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
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class SecurityUserRepository implements SegmantixRepository {
	
	private final EntityManager em;
	private final BaseclassRepository baseclassRepository;

	public SecurityUserRepository(EntityManager em, BaseclassRepository baseclassRepository, SecurityEntityRepository securityEntityRepository) {
		this.em = em;
		this.baseclassRepository = baseclassRepository;
		this.securityEntityRepository = securityEntityRepository;
	}

	private final SecurityEntityRepository securityEntityRepository;

	public List<SecurityUser> listAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityUser> q=cb.createQuery(SecurityUser.class);
		Root<SecurityUser> r=q.from(SecurityUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityUserPredicates(securityUserFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(SecurityUser_.name)));
		TypedQuery<SecurityUser> query = em.createQuery(q);
		BaseclassRepository.addPagination(securityUserFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityUser> void addSecurityUserPredicates(SecurityUserFilter securityUserFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityEntityRepository.addSecurityEntityPredicates(securityUserFilter,cb,q,r,predicates,securityContext);
	}

	public long countAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityUser> r=q.from(SecurityUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityUserPredicates(securityUserFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return baseclassRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return baseclassRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return baseclassRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return baseclassRepository.merge(base);
	}






	public <T> T merge(T base, boolean updateDate) {
		return baseclassRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		baseclassRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		baseclassRepository.massMerge(toMerge, updatedate);
	}
}
