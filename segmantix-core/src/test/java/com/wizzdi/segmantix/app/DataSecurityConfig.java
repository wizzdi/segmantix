package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.service.Cache;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.service.JDBCFieldPath;
import com.wizzdi.segmantix.api.service.JDBCFieldPathProvider;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.api.service.SegmantixCache;
import com.wizzdi.segmantix.service.CriteriaApiSecurityRepository;
import com.wizzdi.segmantix.service.PreparedStatementSecurityRepository;
import com.wizzdi.segmantix.service.SQLSecurityRepository;
import com.wizzdi.segmantix.service.SecurityLinksExtractor;
import jakarta.persistence.criteria.CriteriaBuilder;
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

            @Override
            public <T> Path<String> getInstanceGroupPath(From<?, T> r, CriteriaBuilder cb) {
                return r.get("permissionGroupId");
            }

        };
    }

    @Bean
    public JDBCFieldPathProvider jdbcFieldPathProvider(){
        return new JDBCFieldPathProvider() {
            @Override
            public String getCreatorIdPath(String table) {
                return "creatorId";
            }

            @Override
            public String getTenantIdPath(String table) {
                return "tenantId";
            }

            @Override
            public String getTypePath(String table) {
                return null;
            }

            @Override
            public String getSecurityId(String table) {
                return "id";
            }

            @Override
            public JDBCFieldPath getInstanceGroupPath(String table, String alias) {
                return new JDBCFieldPath("permissionGroupId",null);
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
    public PreparedStatementSecurityRepository preparedStatementSecurityRepository(JDBCFieldPathProvider fieldPathProvider,
                                                                     OperationGroupLinkProvider operationGroupLinkProvider,
                                                                     SecurityLinkProvider securityProvider,
                                                                     Cache cache, OperationService operationService){
        return new PreparedStatementSecurityRepository(fieldPathProvider, new SecurityLinksExtractor(operationGroupLinkProvider, securityProvider,new SegmantixCache(cache,cache),operationService.getAllOps(),SECURITY_WILDCARD_PLACEHOLDER),SECURITY_WILDCARD_PLACEHOLDER);
    }
    @Bean
    public SQLSecurityRepository sqlSecurityRepository(JDBCFieldPathProvider fieldPathProvider,
                                                    OperationGroupLinkProvider operationGroupLinkProvider,
                                                    SecurityLinkProvider securityProvider,
                                                    Cache cache, OperationService operationService){
        return new SQLSecurityRepository(fieldPathProvider, new SecurityLinksExtractor(operationGroupLinkProvider, securityProvider,new SegmantixCache(cache,cache),operationService.getAllOps(),SECURITY_WILDCARD_PLACEHOLDER),SECURITY_WILDCARD_PLACEHOLDER);
    }
    @Bean
    public CriteriaApiSecurityRepository securityRepository(FieldPathProvider fieldPathProvider,
                                                            OperationGroupLinkProvider operationGroupLinkProvider,
                                                            SecurityLinkProvider securityProvider,
                                                            Cache cache, OperationService operationService){
        return new CriteriaApiSecurityRepository(fieldPathProvider, new SecurityLinksExtractor(operationGroupLinkProvider, securityProvider,new SegmantixCache(cache,cache),operationService.getAllOps(),SECURITY_WILDCARD_PLACEHOLDER),SECURITY_WILDCARD_PLACEHOLDER);
    }


}
