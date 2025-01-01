package com.wizzdi.segmantix.spring;

import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.store.jpa.model.TenantToUser;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityUserCreate;
import com.wizzdi.segmantix.store.jpa.request.TenantToUserCreate;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.store.jpa.service.SecurityContextProvider;
import com.wizzdi.segmantix.store.jpa.service.SecurityTenantService;
import com.wizzdi.segmantix.store.jpa.service.SecurityUserService;
import com.wizzdi.segmantix.store.jpa.service.TenantToUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfig {
    @Bean
    public SecurityContext adminSecurityContext(SecurityUserService securityUserService, SecurityTenantService securityTenantService, TenantToUserService tenantToUserService, SecurityContextProvider securityContextProvider){
        SecurityUser admin = securityUserService.createSecurityUser(new SecurityUserCreate().setName("admin"), null);
        SecurityTenant defaultTenant=securityTenantService.createTenant(new SecurityTenantCreate().setName("default tenant"),null);
        TenantToUser link=tenantToUserService.createTenantToUser(new TenantToUserCreate().setTenant(defaultTenant).setUser(admin).setDefaultTenant(true),null);
        return securityContextProvider.getSecurityContext(admin);

    }

}
