package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.OperationGroupLink;
import com.wizzdi.segmantix.impl.model.OperationGroupLink_;
import com.wizzdi.segmantix.impl.model.Operation_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationFilter;
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
import jakarta.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component

public class OperationRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<Operation> listAllOperations(OperationFilter operationFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Operation> q=cb.createQuery(Operation.class);
		Root<Operation> r=q.from(Operation.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Operation> query = em.createQuery(q);
		BasicRepository.addPagination(operationFilter,query);
		return query.getResultList();

	}

	public <T extends Operation> void addOperationPredicates(OperationFilter operationFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(operationFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(operationFilter.getCategories()!=null&&!operationFilter.getCategories().isEmpty()){
			predicates.add(r.get(Operation_.category).in(operationFilter.getCategories()));
		}
		if(operationFilter.getCategoryLike()!=null){
			predicates.add(cb.like(r.get(Operation_.category),operationFilter.getCategoryLike()));
		}
		if(operationFilter.getOperationGroups()!=null&&!operationFilter.getOperationGroups().isEmpty()){
			Join<T, OperationGroupLink> join=r.join(Operation_.operationGroupLinks);
			predicates.add(join.get(OperationGroupLink_.operationGroup).in(operationFilter.getOperationGroups()));

		}
	}

	public long countAllOperations(OperationFilter operationFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Operation> r=q.from(Operation.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}



	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return securedBasicRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		securedBasicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securedBasicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return securedBasicRepository.merge(base);
	}


	public void massMerge(List<?> toMerge) {
		securedBasicRepository.massMerge(toMerge);
	}
}
