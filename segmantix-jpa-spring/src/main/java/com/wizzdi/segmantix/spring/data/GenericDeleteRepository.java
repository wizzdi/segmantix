package com.wizzdi.segmantix.spring.data;

import com.wizzdi.segmantix.store.jpa.data.BaseclassRepository;
import com.wizzdi.segmantix.store.jpa.data.SecuredBasicRepository;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.model.Basic_;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
public class GenericDeleteRepository {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;
	@Autowired
	private BaseclassRepository baseclassRepository;


	public  <T extends Basic> List<T> getObjects(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		if(Baseclass.class.isAssignableFrom(c)){
			securedBasicRepository.addSecuredBasicPredicates(null,cb,q,(From<?, ? extends Baseclass>) r,predicates,securityContext);
		}
		if(Baseclass.class.isAssignableFrom(c)){
			baseclassRepository.addBaseclassPredicates(cb,q,(From<?,? extends Baseclass>)r,predicates,securityContext);
		}
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();

	}

	public Class<?> getType(String className){
		return em.getMetamodel().getEntities().stream().map(f->f.getJavaType()).filter(f->f.getCanonicalName().equals(className)).findFirst().orElse(null);
	}


	public <T> void merge(T t) {
		securedBasicRepository.merge(t);
	}
}
