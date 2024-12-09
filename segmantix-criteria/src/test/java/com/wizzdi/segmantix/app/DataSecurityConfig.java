package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.Cache;
import com.wizzdi.segmantix.api.FieldPathProvider;
import com.wizzdi.segmantix.api.IOperationToGroup;
import com.wizzdi.segmantix.api.ISecurityOperation;
import com.wizzdi.segmantix.api.OperationToGroupService;
import com.wizzdi.segmantix.api.PermissionGroupToBaseclassService;
import com.wizzdi.segmantix.api.Secured;
import com.wizzdi.segmantix.api.SecurityLinkService;
import com.wizzdi.segmantix.impl.service.BaseclassRepository;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSecurityConfig {
    @Bean
    public FieldPathProvider fieldPathProvider(){
        return new FieldPathProvider() {
            @Override
            public <T extends Secured> Path<String> getCreatorIdPath(From<?, T> r) {
                return r.get("creatorId");
            }

            @Override
            public <T extends Secured> Path<String> getTenantIdPath(From<?, T> r) {
                return r.get("tenantId");
            }

            @Override
            public <T extends Secured> Path<String> getTypePath(From<?, T> r) {
                return getTypeFieldOrNull(r,"type");
            }

            @Override
            public <T extends Secured> Path<String> getIdPath(From<?, T> r) {
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
    public BaseclassRepository baseclassRepository(FieldPathProvider fieldPathProvider,
                                                   OperationToGroupService operationToGroupService,
                                                   SecurityLinkService securityLinkService,
                                                   PermissionGroupToBaseclassService permissionGroupToBaseclassService,
                                                   Cache cache,OperationService operationService){
        return new BaseclassRepository(fieldPathProvider,operationToGroupService,securityLinkService,permissionGroupToBaseclassService,cache,cache,operationService.getAllOps());
    }
}
