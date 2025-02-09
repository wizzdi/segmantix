package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.api.service.SegmantixCache;
import com.wizzdi.segmantix.service.CriteriaApiSecurityRepository;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.store.jpa.model.SecurityWildcard;
import jakarta.persistence.EntityManager;

public class SegmantixJPAStore {

    public static CriteriaApiSecurityRepository create(EntityManager em, SegmantixCache segmantixCache, SecurityOperation allOps){
        return new CriteriaApiSecurityRepository(new FieldPathProviderImpl(), new OperationGroupLinkProviderImpl(em),new SecurityProviderImpl(em),segmantixCache,allOps, SecurityWildcard.class.getSimpleName());
    }


}
