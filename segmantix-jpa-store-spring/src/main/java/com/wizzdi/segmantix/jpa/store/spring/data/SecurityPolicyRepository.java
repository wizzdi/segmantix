package com.wizzdi.segmantix.jpa.store.spring.data;


import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.model.security.SecurityPolicy_;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyFilter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component

public class SecurityPolicyRepository  {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<SecurityPolicy> listAllSecurityPolicies(SecurityPolicyFilter SecurityPolicyFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SecurityPolicy> q = cb.createQuery(SecurityPolicy.class);
		Root<SecurityPolicy> r = q.from(SecurityPolicy.class);
		List<Predicate> predicates = new ArrayList<>();
		addSecurityPolicyPredicates(SecurityPolicyFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<SecurityPolicy> query = em.createQuery(q);
		BasicRepository.addPagination(SecurityPolicyFilter, query);
		return query.getResultList();

	}

	public <T extends SecurityPolicy> void addSecurityPolicyPredicates(SecurityPolicyFilter securityPolicyFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityPolicyFilter.getBasicPropertiesFilter(), cb, q, r, predicates, securityContext);
		if (securityPolicyFilter.getEnabled() != null) {
			predicates.add(cb.equal(r.get(SecurityPolicy_.enabled), securityPolicyFilter.getEnabled()));
		}

		if (securityPolicyFilter.getStartTime() != null) {
			predicates.add(cb.lessThanOrEqualTo(r.get(SecurityPolicy_.startTime), securityPolicyFilter.getStartTime()));
		}
		if (securityPolicyFilter.getTenants() != null && !securityPolicyFilter.getTenants().isEmpty()) {
			Set<String> ids = securityPolicyFilter.getTenants().stream().map(f -> f.getId()).collect(Collectors.toSet());
			Join<T, Tenant> join = r.join(SecurityPolicy_.policyTenant);
			predicates.add(join.get(Tenant_.id).in(ids));
		}
		Set<String> roleIds = new HashSet<>();
		if (securityPolicyFilter.isIncludeNoRole()) {
			roleIds.add(null);
		}
		if(securityPolicyFilter.getRoles()!=null){
			roleIds.addAll(securityPolicyFilter.getRoles().stream().map(f->f.getId()).collect(Collectors.toSet()));
		}
		if (!roleIds.isEmpty()) {
			Join<T, Role> join = r.join(SecurityPolicy_.policyRole);
			predicates.add(join.get(Role_.id).in(roleIds));
		}


	}

	public long countAllSecurityPolicies(SecurityPolicyFilter SecurityPolicyFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SecurityPolicy> r = q.from(SecurityPolicy.class);
		List<Predicate> predicates = new ArrayList<>();
		addSecurityPolicyPredicates(SecurityPolicyFilter, cb, q, r, predicates, securityContext);
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

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T extends SecurityPolicy> T getSecurityPolicyByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {

		return securedBasicRepository.getByIdOrNull(id,c,SecurityPolicy_.security,securityContext);
	}

	public <T extends SecurityPolicy> List<T> listSecurityPolicyByIds( Class<T> c,Set<String> ids, SecurityContext securityContext) {

		return securedBasicRepository.listByIds(c,ids,SecurityPolicy_.security,securityContext);
	}
}