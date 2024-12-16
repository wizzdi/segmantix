package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.api.service.InstanceGroupLinkProvider;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.service.SecurityRepository;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSecurityConfig {
    public static final String SECURITY_WILDCARD_PLACEHOLDER="***WILDCARD_TYPE***";

    @Bean
    public FieldPathProvider fieldPathProvider(){
        return new FieldPathProvider() {
            @Override
            public <T> Path<String> getCreatorIdPath(From<?, T> r) {
                return r.get("creatorId");
            }

            @Override
            public <T> Path<String> getTenantIdPath(From<?, T> r) {
                return r.get("tenantId");
            }

            @Override
            public <T> Path<String> getTypePath(From<?, T> r) {
                return getTypeFieldOrNull(r,"type");
            }

            @Override
            public <T> Path<String> getSecurityId(From<?, T> r) {
                return r.get("id");
            }
        };
    }
    private Path<String> getTypeFieldOrNull(From<?,?> r,String name){
        try {
            return r.get(name);
        }
        catch (Throwable e){
            return null;
        }
    }
    @Bean
    public SecurityRepository securityRepository(FieldPathProvider fieldPathProvider,
                                                 OperationGroupLinkProvider operationGroupLinkProvider,
                                                 SecurityLinkProvider securityProvider,
                                                 InstanceGroupLinkProvider instanceGroupLinkProvider,
                                                 Cache cache, OperationService operationService){
        return new SecurityRepository(fieldPathProvider, operationGroupLinkProvider, securityProvider, instanceGroupLinkProvider,cache,cache,operationService.getAllOps(),SECURITY_WILDCARD_PLACEHOLDER);
    }
}
