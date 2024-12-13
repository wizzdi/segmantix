package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.Baseclass_;
import com.wizzdi.segmantix.impl.model.InstanceGroupLink;
import com.wizzdi.segmantix.impl.model.InstanceGroupLink_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component

public class InstanceGroupLinkRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<InstanceGroupLink> listAllInstanceGroupLinks(InstanceGroupLinkFilter instanceGroupLinkFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<InstanceGroupLink> q=cb.createQuery(InstanceGroupLink.class);
		Root<InstanceGroupLink> r=q.from(InstanceGroupLink.class);
		List<Predicate> predicates=new ArrayList<>();
		addInstanceGroupLinkPredicates(instanceGroupLinkFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(getSorting(instanceGroupLinkFilter, r, cb));
		TypedQuery<InstanceGroupLink> query = em.createQuery(q);
		BasicRepository.addPagination(instanceGroupLinkFilter,query);
		return query.getResultList();

	}

	private static Order getSorting(InstanceGroupLinkFilter instanceGroupLinkFilter, Root<InstanceGroupLink> r, CriteriaBuilder cb) {
		InstanceGroupLinkFilter.Sorting sorting = instanceGroupLinkFilter.getSorting();
		if (sorting == null) {
			sorting=new InstanceGroupLinkFilter.Sorting(InstanceGroupLinkFilter.SortBy.BASECLASS_NAME,true);
		}
		Path<?> sortPath = switch (sorting.sortBy()) {
			case CLAZZ_NAME -> r.join(InstanceGroupLink_.baseclass).get(Baseclass_.clazz).get(Baseclass_.name);
			case BASECLASS_ID -> r.join(InstanceGroupLink_.baseclass).get(Baseclass_.id);
			case BASECLASS_NAME -> r.join(InstanceGroupLink_.baseclass).get(Baseclass_.name);
			case BASECLASS_CREATION_DATE -> r.join(InstanceGroupLink_.baseclass).get(Baseclass_.creationDate);
		};
        return sorting.asc() ? cb.asc(sortPath) : cb.desc(sortPath);
	}

	public  <T extends InstanceGroupLink> void addInstanceGroupLinkPredicates(InstanceGroupLinkFilter instanceGroupLinkFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(instanceGroupLinkFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if (instanceGroupLinkFilter.getBaseclasses() != null && !instanceGroupLinkFilter.getBaseclasses().isEmpty()) {
			predicates.add(r.get(InstanceGroupLink_.baseclass).in(instanceGroupLinkFilter.getBaseclasses()));
		}
		if (instanceGroupLinkFilter.getClazzes() != null && !instanceGroupLinkFilter.getClazzes().isEmpty()) {
			Join<T, Baseclass> baseclassJoin = r.join(InstanceGroupLink_.baseclass);
			predicates.add(baseclassJoin.get(Baseclass_.clazz).in(instanceGroupLinkFilter.getClazzes()));
		}
		if (instanceGroupLinkFilter.getInstanceGroups() != null && instanceGroupLinkFilter.getInstanceGroups().isEmpty()) {
			predicates.add(r.get(InstanceGroupLink_.instanceGroup).in(instanceGroupLinkFilter.getInstanceGroups()));
		}
	}

	public long countAllInstanceGroupLinks(InstanceGroupLinkFilter instanceGroupLinkFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<InstanceGroupLink> r=q.from(InstanceGroupLink.class);
		List<Predicate> predicates=new ArrayList<>();
		addInstanceGroupLinkPredicates(instanceGroupLinkFilter,cb,q,r,predicates,securityContext);
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
