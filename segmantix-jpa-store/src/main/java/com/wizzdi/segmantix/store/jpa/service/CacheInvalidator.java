package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.SegmantixCache;
import com.wizzdi.segmantix.store.jpa.interfaces.MergeListener;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CacheInvalidator implements MergeListener, SegmantixService {
    private static final Logger logger= LoggerFactory.getLogger(CacheInvalidator.class);
    private final Cache operationToOperationGroupCache;
    private final Cache dataAccessControlCache;

    public CacheInvalidator(SegmantixCache segmantixCache) {
        this.operationToOperationGroupCache = segmantixCache.operationToOperationGroupCache();
        this.dataAccessControlCache = segmantixCache.dataAccessControlCache();
    }

    @Override
    public void onCreate(Object t) {
        switch (t){
            case SecurityLink securityLink->invalidateCache(securityLink);
            case OperationToGroup operationToGroup->invalidateCache(operationToGroup);
            default -> {}
        }
    }

    @Override
    public void onUpdate(Object t) {

        switch (t){
            case SecurityLink securityLink->invalidateCache(securityLink);
            case OperationToGroup operationToGroup->invalidateCache(operationToGroup);
            default -> {}
        }
    }


    private void invalidateCache(OperationToGroup operationToGroup) {
        if(operationToGroup.getOperation()==null){
            return;
        }
        operationToOperationGroupCache.remove(operationToGroup.getOperation().getId());
        logger.debug("evicted operation cache {}", operationToGroup.getOperation().getId());
    }


    private void invalidateCache(SecurityLink link) {
        if (link.getSecurityEntity() != null) {
            dataAccessControlCache.remove(link.getSecurityEntity().getId());
            logger.debug("evicted security entity cache {}", link.getSecurityEntity().getId());
        }
    }
}
