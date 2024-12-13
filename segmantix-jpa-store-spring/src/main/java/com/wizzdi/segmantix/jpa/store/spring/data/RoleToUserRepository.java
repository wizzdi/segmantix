package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.RoleToUser;
import com.wizzdi.segmantix.impl.model.RoleToUser_;
import com.wizzdi.segmantix.impl.model.Role_;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.impl.model.User_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserFilter;
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

public class RoleToUserRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<RoleToUser> listAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<RoleToUser> q=cb.createQuery(RoleToUser.class);
		Root<RoleToUser> r=q.from(RoleToUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToUserPredicates(roleToUserFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<RoleToUser> query = em.createQuery(q);
		BasicRepository.addPagination(roleToUserFilter,query);
		return query.getResultList();

	}

	public <T extends RoleToUser> void addRoleToUserPredicates(RoleToUserFilter roleToUserFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(roleToUserFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(roleToUserFilter.getRoles()!=null&&!roleToUserFilter.getRoles().isEmpty()){
			Set<String> ids=roleToUserFilter.getRoles().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Role> join=r.join(RoleToUser_.role);
			predicates.add(join.get(Role_.id).in(ids));
		}

		if(roleToUserFilter.getUsers()!=null&&!roleToUserFilter.getUsers().isEmpty()){
			Set<String> ids=roleToUserFilter.getUsers().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, User> join=r.join(RoleToUser_.user);
			predicates.add(join.get(User_.id).in(ids));
		}
	}

	public long countAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<RoleToUser> r=q.from(RoleToUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToUserPredicates(roleToUserFilter,cb,q,r,predicates,securityContext);
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

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}
}
