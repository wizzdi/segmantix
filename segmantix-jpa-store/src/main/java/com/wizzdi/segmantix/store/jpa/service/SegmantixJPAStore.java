package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.service.SecurityRepository;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.RoleToUser;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityWildcard;
import com.wizzdi.segmantix.store.jpa.model.TenantToUser;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SegmantixJPAStore {

    public static SecurityRepository create(EntityManager em,Cache dataAccessControlCache, Cache operationToOperationGroupCache, SecurityOperation allOps){
        return new SecurityRepository(new FieldPathProviderImpl(), new OperationGroupLinkProviderImpl(em),new SecurityProviderImpl(em),dataAccessControlCache,operationToOperationGroupCache,allOps, SecurityWildcard.class.getSimpleName());
    }


}
