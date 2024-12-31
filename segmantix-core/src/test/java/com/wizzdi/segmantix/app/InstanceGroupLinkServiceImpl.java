package com.wizzdi.segmantix.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;


public class InstanceGroupLinkServiceImpl {
    
    private EntityManager em;

    @Transactional
    public void clear() {
        em.createNativeQuery("update test_entity set permission_group_id=null").executeUpdate();
    }
}
