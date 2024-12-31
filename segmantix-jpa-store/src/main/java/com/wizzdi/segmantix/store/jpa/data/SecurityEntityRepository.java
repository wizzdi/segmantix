package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.SecurityEntity;

import com.wizzdi.segmantix.store.jpa.request.SecurityEntityFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;


import java.util.List;



public class SecurityEntityRepository implements SegmantixRepository {

	private final SecuredBasicRepository securedBasicRepository;

    public SecurityEntityRepository(SecuredBasicRepository securedBasicRepository) {
        this.securedBasicRepository = securedBasicRepository;
    }


    public <T extends SecurityEntity> void addSecurityEntityPredicates(SecurityEntityFilter securityEntityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityEntityFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
	}

}
