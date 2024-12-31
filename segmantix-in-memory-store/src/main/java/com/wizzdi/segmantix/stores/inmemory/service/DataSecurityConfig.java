package com.wizzdi.segmantix.stores.inmemory.service;

import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.service.SecurityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class DataSecurityConfig {
    public static final String SECURITY_WILDCARD_PLACEHOLDER="***WILDCARD_TYPE***";

    @Bean
    public FieldPathProvider fieldPathProvider(){
        return new FieldPathProviderImpl();
    }
    @Bean
    public SecurityRepository securityRepository(FieldPathProvider fieldPathProvider,
                                                 OperationGroupLinkProvider operationGroupLinkProvider,
                                                 SecurityLinkProvider securityProvider,
                                                 Cache cache, OperationService operationService){
        return new SecurityRepository(fieldPathProvider, operationGroupLinkProvider, securityProvider,cache,cache,operationService.getAllOps(),SECURITY_WILDCARD_PLACEHOLDER);
    }

}
