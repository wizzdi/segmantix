package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzFilter;
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
import java.util.stream.Collectors;

@Component

public class OperationToClazzRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<OperationToClazz> listAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<OperationToClazz> q=cb.createQuery(OperationToClazz.class);
		Root<OperationToClazz> r=q.from(OperationToClazz.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToClazzPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<OperationToClazz> query = em.createQuery(q);
		BasicRepository.addPagination(operationToClazzFilter,query);
		return query.getResultList();

	}

	public  <T extends OperationToClazz> void addOperationToClazzPredicates(OperationToClazzFilter operationToClazzFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		BasicRepository.addBasicPropertiesFilter(operationToClazzFilter.getBasicPropertiesFilter(),cb,q,r,predicates);
		if(operationToClazzFilter.getClazzes()!=null&&!operationToClazzFilter.getClazzes().isEmpty()){
			Set<String> ids=operationToClazzFilter.getClazzes().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Clazz> join=r.join(OperationToClazz_.clazz);
			predicates.add(join.get(Clazz_.id).in(ids));
		}

		if(operationToClazzFilter.getOperations()!=null&&!operationToClazzFilter.getOperations().isEmpty()){
			Set<String> ids=operationToClazzFilter.getOperations().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Operation> join=r.join(OperationToClazz_.operation);
			predicates.add(join.get(Operation_.id).in(ids));
		}
	}

	public long countAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<OperationToClazz> r=q.from(OperationToClazz.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToClazzPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
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
}
