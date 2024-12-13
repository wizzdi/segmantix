package com.wizzdi.segmantix.jpa.store.spring.configuration;


import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.RoleToUser;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.TenantToUser;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.segmantix.jpa.store.spring.interfaces.SecurityContextProvider;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserFilter;
import com.wizzdi.segmantix.jpa.store.spring.service.RoleToUserService;
import com.wizzdi.segmantix.jpa.store.spring.service.TenantToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnMissingBean(SecurityContextProvider.class)
public class DefaultSecurityContextProviderConfiguration {

    @Autowired
    private TenantToUserService tenantToUserService;
    @Autowired
    private RoleToUserService roleToUserService;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SecurityContextProvider securityContextProvider() {
        return this::getSecurityContext;
    }

    private SecurityContext getSecurityContext(User user, Operation operation) {
        List<TenantToUser> links=tenantToUserService.listAllTenantToUsers(new TenantToUserFilter().setUsers(Collections.singletonList(user)),null);
        List<RoleToUser> roleToUsers=roleToUserService.listAllRoleToUsers(new RoleToUserFilter().setUsers(Collections.singletonList(user)),null);
        List<IRole> allRoles=new ArrayList<>(roleToUsers.stream().map(f->f.getRole()).collect(Collectors.toMap(f->f.getId(), f->f,(a, b)->a)).values());
        List<ITenant> tenants = new ArrayList<>(links.stream().map(f -> f.getTenant()).collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a)).values());
        Tenant tenantToCreateIn = links.stream().filter(f -> f.isDefaultTenant()).map(f -> f.getTenant()).findFirst().orElse(null);
        return new SecurityContext(user,tenants,tenantToCreateIn,allRoles,operation);
    }
}
