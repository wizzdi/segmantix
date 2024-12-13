package com.wizzdi.segmantix.jpa.store.spring.data;


import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.Baseclass_;
import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.RoleToUser;
import com.wizzdi.segmantix.impl.model.RoleToUser_;
import com.wizzdi.segmantix.impl.model.Role_;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.Tenant_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.RoleFilter;
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

public class RoleRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;

	@Autowired
	private SecurityEntityRepository securityEntityRepository;

	public List<Role> listAllRoles(RoleFilter roleFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Role> q=cb.createQuery(Role.class);
		Root<Role> r=q.from(Role.class);
		List<Predicate> predicates=new ArrayList<>();
		addRolePredicates(roleFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Role_.name)));
		TypedQuery<Role> query = em.createQuery(q);
		BaseclassRepository.addPagination(roleFilter,query);
		return query.getResultList();

	}

	public <T extends Role> void addRolePredicates(RoleFilter roleFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityEntityRepository.addSecurityEntityPredicates(roleFilter,cb,q,r,predicates,securityContext);
		if(roleFilter.getTenants()!=null &&!roleFilter.getTenants().isEmpty()){
			Set<String> ids=roleFilter.getTenants().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Baseclass> baseclassJoin=r.join(Role_.security);
			Join<Baseclass, Tenant> join=baseclassJoin.join(Baseclass_.tenant);
			predicates.add(join.get(Tenant_.id).in(ids));
		}
		if(roleFilter.getUsers()!=null &&!roleFilter.getUsers().isEmpty()){
			Join<T, RoleToUser> roleToUserJoin=r.join(Role_.roleToUser);
			predicates.add(roleToUserJoin.get(RoleToUser_.user).in(roleFilter.getUsers()));
		}
	}

	public long countAllRoles(RoleFilter roleFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Role> r=q.from(Role.class);
		List<Predicate> predicates=new ArrayList<>();
		addRolePredicates(roleFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return baseclassRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		baseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return baseclassRepository.findByIdOrNull(type, id);
	}
}