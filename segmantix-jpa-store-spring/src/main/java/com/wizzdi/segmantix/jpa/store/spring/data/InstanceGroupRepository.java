package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.impl.model.InstanceGroup_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupFilter;
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

public class InstanceGroupRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;




	public List<InstanceGroup> listAllInstanceGroups(InstanceGroupFilter instanceGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<InstanceGroup> q=cb.createQuery(InstanceGroup.class);
		Root<InstanceGroup> r=q.from(InstanceGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addInstanceGroupPredicates(instanceGroupFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<InstanceGroup> query = em.createQuery(q);
		BasicRepository.addPagination(instanceGroupFilter,query);
		return query.getResultList();

	}

	public <T extends InstanceGroup> void addInstanceGroupPredicates(InstanceGroupFilter instanceGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(instanceGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(instanceGroupFilter.getExternalIds()!=null&&!instanceGroupFilter.getExternalIds().isEmpty()){
			predicates.add(r.get(InstanceGroup_.externalId).in(instanceGroupFilter.getExternalIds()));
		}
	}

	public long countAllInstanceGroups(InstanceGroupFilter instanceGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<InstanceGroup> r=q.from(InstanceGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addInstanceGroupPredicates(instanceGroupFilter,cb,q,r,predicates,securityContext);
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}
}
