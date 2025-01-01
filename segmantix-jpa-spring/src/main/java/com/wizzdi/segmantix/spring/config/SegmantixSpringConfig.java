package com.wizzdi.segmantix.spring.config;

import com.wizzdi.segmantix.api.service.SegmantixCache;
import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.service.SecurityRepository;
import com.wizzdi.segmantix.store.jpa.data.Operations;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.store.jpa.service.SecurityOperationService;
import com.wizzdi.segmantix.store.jpa.service.SegmantixJPAStore;
import jakarta.persistence.EntityManager;
import org.eclipse.persistence.internal.oxm.schema.model.All;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@Configuration
@ComponentScans(value = {
        @ComponentScan(
                basePackages = "com.wizzdi.segmantix.store.jpa",
                includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SegmantixService.class, SegmantixRepository.class}),
                useDefaultFilters = false
        ),
       @ComponentScan(basePackages = "com.wizzdi.segmantix.spring")
})
@EnableJpaRepositories
public class SegmantixSpringConfig {

    @Bean
    public SecurityRepository securityRepository(EntityManager em, SegmantixCache segmantixCache, SecurityOperationService securityOperationService){
        return SegmantixJPAStore.create(em,segmantixCache,securityOperationService.getAllOperations());
    }
    @Bean
    public SegmantixCache segmantixCacheProvider(Cache dataAccessControlCache, Cache operationToOperationGroupCache){
        return new SegmantixCache(new CacheWrapper(dataAccessControlCache),new CacheWrapper(operationToOperationGroupCache));
    }

    @Bean
    @ConditionalOnMissingBean
    public Operations operations(){
        return new Operations(
                List.of(
                        SecurityOperation.ofStandardAccess(All.class,SecurityOperationService.getStandardAccessId(All.class),"All Operations","All Operations", Access.allow,null)
                )
        );
    }
}
