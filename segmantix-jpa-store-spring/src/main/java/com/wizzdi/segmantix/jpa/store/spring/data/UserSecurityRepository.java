package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.UserSecurity;
import com.wizzdi.segmantix.impl.model.UserSecurity_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityFilter;
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

public class UserSecurityRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecurityApiRepository securityApiRepository;


	public List<UserSecurity> listAllUserSecuritys(UserSecurityFilter userSecurityFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<UserSecurity> q=cb.createQuery(UserSecurity.class);
		Root<UserSecurity> r=q.from(UserSecurity.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserSecurityPredicates(userSecurityFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<UserSecurity> query = em.createQuery(q);
		BasicRepository.addPagination(userSecurityFilter,query);
		return query.getResultList();

	}

	public <T extends UserSecurity> void addUserSecurityPredicates(UserSecurityFilter userSecurityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityApiRepository.addSecurityPredicates(userSecurityFilter,cb,q,r,predicates,securityContext);
		if(userSecurityFilter.getUsers()!=null&&!userSecurityFilter.getUsers().isEmpty()){
			predicates.add(r.get(UserSecurity_.user).in(userSecurityFilter.getUsers()));
		}
	}

	public long countAllUserSecuritys(UserSecurityFilter userSecurityFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<UserSecurity> r=q.from(UserSecurity.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserSecurityPredicates(userSecurityFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securityApiRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		securityApiRepository.massMerge(list);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityApiRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityApiRepository.getByIdOrNull(id, c, securityContext);
	}
}
