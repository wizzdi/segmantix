package com.wizzdi.segmantix.jpa.store.spring.data;

import com.flexicore.model.SecurityEntity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityEntityFilter;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SecurityEntityRepository  {

	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public <T extends SecurityEntity> void addSecurityEntityPredicates(SecurityEntityFilter securityEntityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityEntityFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
	}

}
