package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass_;
import com.wizzdi.segmantix.store.jpa.request.UserToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class UserToBaseclassRepository implements SegmantixRepository {
	
	private final EntityManager em;
	private final SecurityLinkRepository securityLinkRepository;

    public UserToBaseclassRepository(EntityManager em, SecurityLinkRepository securityLinkRepository) {
        this.em = em;
        this.securityLinkRepository = securityLinkRepository;
    }


    public List<UserToBaseclass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<UserToBaseclass> q=cb.createQuery(UserToBaseclass.class);
		Root<UserToBaseclass> r=q.from(UserToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<UserToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(userToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends UserToBaseclass> void addUserToBaseclassPredicates(UserToBaseclassFilter userToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		if(userToBaseclassFilter.getUsers()!=null&&!userToBaseclassFilter.getUsers().isEmpty()){
			predicates.add(r.get(UserToBaseclass_.user).in(userToBaseclassFilter.getUsers()));
		}
	}

	public long countAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<UserToBaseclass> r=q.from(UserToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securityLinkRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityLinkRepository.getByIdOrNull(id, c, securityContext);
	}
}
