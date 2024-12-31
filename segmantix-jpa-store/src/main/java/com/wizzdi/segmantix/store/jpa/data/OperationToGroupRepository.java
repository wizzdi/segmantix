package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup_;

import com.wizzdi.segmantix.store.jpa.request.OperationToGroupFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
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
import java.util.stream.Collectors;



public class OperationToGroupRepository  implements SegmantixRepository {
	
	private final EntityManager em;
	private final SecuredBasicRepository securedBasicRepository;

	public OperationToGroupRepository(EntityManager em, SecuredBasicRepository securedBasicRepository) {
		this.em = em;
		this.securedBasicRepository = securedBasicRepository;
	}

	public List<OperationToGroup> listAllOperationToGroups(OperationToGroupFilter operationFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<OperationToGroup> q=cb.createQuery(OperationToGroup.class);
		Root<OperationToGroup> r=q.from(OperationToGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToGroupPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<OperationToGroup> query = em.createQuery(q);
		BasicRepository.addPagination(operationFilter,query);
		return query.getResultList();

	}

	public <T extends OperationToGroup> void addOperationToGroupPredicates(OperationToGroupFilter operationFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(operationFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(operationFilter.getOperationGroups()!=null&&!operationFilter.getOperationGroups().isEmpty()){
			predicates.add(r.get(OperationToGroup_.operationGroup).in(operationFilter.getOperationGroups()));
		}
		if(operationFilter.getOperations()!=null&&!operationFilter.getOperations().isEmpty()){
			Set<String> ids=operationFilter.getOperations().stream().map(f->f.getId()).collect(Collectors.toSet());
			predicates.add(r.get(OperationToGroup_.operationId).in(ids));
		}
	}

	public long countAllOperationToGroups(OperationToGroupFilter operationFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<OperationToGroup> r=q.from(OperationToGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToGroupPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

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
