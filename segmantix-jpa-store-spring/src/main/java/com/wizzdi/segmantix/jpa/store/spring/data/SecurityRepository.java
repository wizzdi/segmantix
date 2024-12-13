package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.RoleSecurity;
import com.wizzdi.segmantix.impl.model.Security;
import com.wizzdi.segmantix.impl.model.Security_;
import com.wizzdi.segmantix.impl.model.TenantSecurity;
import com.wizzdi.segmantix.impl.model.UserSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
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

public class SecurityRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<Security> listAllSecuritys(SecurityFilter securityFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Security> q=cb.createQuery(Security.class);
		Root<Security> r=q.from(Security.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityPredicates(securityFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		if(securityFilter.getSorting()!=null){
			Order order=getOrder(cb,r, securityFilter.getSorting());
			q=q.orderBy(order);
		}
		TypedQuery<Security> query = em.createQuery(q);
		BasicRepository.addPagination(securityFilter,query);
		return query.getResultList();

	}

	public Order getOrder(CriteriaBuilder cb, From<?,Security> r, List<SecurityOrder> sorting) {
		if(sorting.isEmpty()){
			return null;
		}
		CriteriaBuilder.SimpleCase<String, Object> switchCase = cb.selectCase(r.get(Security_.dtype));
		int index=0;
		for (SecurityOrder securityOrder : sorting) {
			switch (securityOrder){
				case ROLE -> switchCase=switchCase.when(RoleSecurity.class.getSimpleName(),index);
				case USER -> switchCase=switchCase.when(UserSecurity.class.getSimpleName(),index);
				case TENANT -> switchCase=switchCase.when(TenantSecurity.class.getSimpleName(),index);
			}
			index++;
		}
		switchCase.otherwise(SecurityOrder.values().length);
		return cb.asc(switchCase);
	}

	public <T extends Security> void addSecurityPredicates(SecurityFilter securityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityFilter.getSecurityGroups()!=null&&!securityFilter.getSecurityGroups().isEmpty()){
			predicates.add(r.get(Security_.securityGroup).in(securityFilter.getSecurityGroups()));
		}
		if(securityFilter.getBaseclasses()!=null&&!securityFilter.getBaseclasses().isEmpty()){
			predicates.add(r.get(Security_.baseclass).in(securityFilter.getBaseclasses()));
		}
		if(securityFilter.getClazzes()!=null&&!securityFilter.getClazzes().isEmpty()){
			predicates.add(r.get(Security_.clazz).in(securityFilter.getClazzes()));
		}

		if(securityFilter.getInstanceGroups()!=null&&!securityFilter.getInstanceGroups().isEmpty()){
			predicates.add(r.get(Security_.instanceGroup).in(securityFilter.getInstanceGroups()));
		}
		if(securityFilter.getOperationGroups()!=null&&!securityFilter.getOperationGroups().isEmpty()){
			predicates.add(r.get(Security_.operationGroup).in(securityFilter.getOperationGroups()));
		}
		if(securityFilter.getAccesses()!=null&&!securityFilter.getAccesses().isEmpty()){
			predicates.add(r.get(Security_.access).in(securityFilter.getAccesses()));
		}
		if(securityFilter.getOperations()!=null&&!securityFilter.getOperations().isEmpty()){
			predicates.add(r.get(Security_.operation).in(securityFilter.getOperations()));
		}
		if(securityFilter.getRelevantUsers()!=null&&!securityFilter.getRelevantUsers().isEmpty()){
			Path<Security> p = (Path<Security>) r;
			Predicate pred=cb.and(
					cb.equal(r.type(), UserSecurity.class),
					cb.treat(p, UserSecurity.class).get(UserSecurity_.user).in(securityFilter.getRelevantUsers()));
			if(securityFilter.getRelevantRoles()!=null&&!securityFilter.getRelevantRoles().isEmpty()){
				pred=cb.or(pred,cb.and(
						cb.equal(r.type(), RoleSecurity.class),
						cb.treat(p, RoleSecurity.class).get(RoleSecurity_.role).in(securityFilter.getRelevantRoles())));
			}
			if(securityFilter.getRelevantTenants()!=null&&!securityFilter.getRelevantTenants().isEmpty()){
				pred=cb.or(pred,cb.and(
						cb.equal(r.type(), TenantSecurity.class),
						cb.treat(p, TenantSecurity.class).get(TenantSecurity_.tenant).in(securityFilter.getRelevantTenants())));
			}
			predicates.add(pred);
		}
	}

	public long countAllSecuritys(SecurityFilter securityFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Security> r=q.from(Security.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityPredicates(securityFilter,cb,q,r,predicates,securityContext);
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
