package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.Security;
import com.wizzdi.segmantix.impl.model.SecurityGroup;
import com.wizzdi.segmantix.impl.model.SecurityGroup_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityGroupFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component

public class SecurityGroupRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;
	@Autowired
	private SecurityApiRepository securityApiRepository;


	public List<SecurityGroup> listAllSecurityGroups(SecurityGroupFilter securityGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityGroup> q=cb.createQuery(SecurityGroup.class);
		Root<SecurityGroup> r=q.from(SecurityGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		Join<SecurityGroup, Security> join = addSecurityGroupPredicates(securityGroupFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		if(securityGroupFilter.getSecurityFilter()!=null&&securityGroupFilter.getSecurityFilter().getSorting()!=null&&join!=null){
			Order order= securityApiRepository.getOrder(cb,join,securityGroupFilter.getSecurityFilter().getSorting());
			q=q.orderBy(order);
		}
		TypedQuery<SecurityGroup> query = em.createQuery(q);
		BasicRepository.addPagination(securityGroupFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityGroup> Join<T, Security> addSecurityGroupPredicates(SecurityGroupFilter securityGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityGroupFilter.getSecurityFilter()!=null){
			Join<T,Security> join=r.join(SecurityGroup_.securitys);
			securityApiRepository.addSecurityPredicates(securityGroupFilter.getSecurityFilter(),cb,q,join,predicates,securityContext);
			return join;
		}
		return null;

	}

	public long countAllSecurityGroups(SecurityGroupFilter securityGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityGroup> r=q.from(SecurityGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityGroupPredicates(securityGroupFilter,cb,q,r,predicates,securityContext);
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
