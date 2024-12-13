package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.model.Role;
import com.flexicore.model.RoleToUser;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.model.TenantToUser;
import com.wizzdi.segmantix.jpa.store.spring.interfaces.DefaultRoleProvider;
import com.wizzdi.segmantix.jpa.store.spring.interfaces.DefaultRoleToUserProvider;
import com.wizzdi.segmantix.jpa.store.spring.interfaces.DefaultTenantProvider;
import com.wizzdi.segmantix.jpa.store.spring.interfaces.DefaultTenantToUserProvider;
import com.wizzdi.segmantix.jpa.store.spring.interfaces.DefaultUserProvider;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.UserCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserCreate;
import com.wizzdi.segmantix.jpa.store.spring.response.Clazzes;
import com.wizzdi.segmantix.jpa.store.spring.response.DefaultSecurityEntities;
import com.wizzdi.segmantix.jpa.store.spring.response.TenantAndUserInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DefaultObjectsCreator {
    private static final Logger logger= LoggerFactory.getLogger(DefaultObjectsCreator.class);

    private static final String DEFAULT_TENANT_ID = "jgV8M9d0Qd6owkPPFrbWIQ";
    private static final String TENANT_TO_USER_ID = "Xk5siBx+TyWv+G6V+XuSdw";
    private static final String SUPER_ADMIN_ROLE_ID = "HzFnw-nVR0Olq6WBvwKcQg";
    private static final String SUPER_ADMIN_TO_ADMIN_ID = "EbVFgr+YS3ezYUblzceVGA";








    @ConditionalOnMissingBean
    @Bean
    public DefaultTenantProvider<Tenant> defaultTenantProvider(TenantService tenantService) {
        return tenantCreate -> tenantService.createTenant(tenantCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultUserProvider<User> defaultUserProvider(UserService userService) {
        return userCreate -> userService.createUser(userCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultTenantToUserProvider<TenantToUser> defaultTenantToUserProvider(TenantToUserService tenantToUserService) {
        return tenantToUserCreate -> tenantToUserService.createTenantToUser(tenantToUserCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultRoleProvider<Role> defaultRoleProvider(RoleService roleService) {
        return roleCreate -> roleService.createRole(roleCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultRoleToUserProvider<RoleToUser> defaultRoleToUserProvider(RoleToUserService roleToUserService) {
        return roleToUserCreate -> roleToUserService.createRoleToUser(roleToUserCreate, null);
    }


    @Bean
    @ConditionalOnMissingBean
    public TenantAndUserInit tenantAndUserInit(DefaultTenantProvider defaultTenantProvider, DefaultUserProvider defaultUserProvider, DefaultTenantToUserProvider defaultTenantToUserProvider,
                                               @Qualifier("systemAdminId") String systemAdminId,UserService userService,TenantToUserService tenantToUserService,TenantService tenantService) {
        TenantCreate tenantCreate = new TenantCreate()
                .setIdForCreate(DEFAULT_TENANT_ID)
                .setName("Default Tenant")
                .setDescription("Default Tenant");
        Tenant defaultTenant = tenantService.findByIdOrNull(Tenant.class, DEFAULT_TENANT_ID);
        if (defaultTenant == null) {
            logger.debug("Creating Default Tenant");
            defaultTenant = defaultTenantProvider.createDefaultTenant(tenantCreate);
        }
        if (defaultTenant.getSecurity().getTenant() == null) {
            defaultTenant.getSecurity().setTenant(defaultTenant);
            defaultTenant = tenantService.merge(defaultTenant);
        }

        UserCreate userCreate = new UserCreate()
                .setName("Admin")
                .setIdForCreate(systemAdminId);
        User admin = userService.findByIdOrNull(User.class, systemAdminId);
        if (admin == null) {
            logger.debug("Creating Admin User");
            admin = defaultUserProvider.createUser(userCreate);
        }

        if (admin.getSecurity().getCreator() == null) {
            admin.getSecurity().setCreator(admin);
            admin = userService.merge(admin);
        }
        if (admin.getSecurity().getTenant() == null) {
            admin.getSecurity().setTenant(defaultTenant);
            admin = userService.merge(admin);
        }


        if (defaultTenant.getSecurity().getCreator() == null) {
            defaultTenant.getSecurity().setCreator(admin);
            defaultTenant = tenantService.merge(defaultTenant);
        }

        TenantToUserCreate tenantToUserCreate = new TenantToUserCreate()
                .setDefaultTenant(true)
                .setUser(admin)
                .setTenant(defaultTenant)
                .setIdForCreate(TENANT_TO_USER_ID);
        TenantToUser tenantToUser = tenantToUserService.findByIdOrNull(TenantToUser.class, TENANT_TO_USER_ID);
        if (tenantToUser == null) {
            logger.debug("Creating Tenant To User link");
            tenantToUser = defaultTenantToUserProvider.createTenantToUser(tenantToUserCreate);
        }

        if (tenantToUser.getSecurity().getCreator() == null) {
            tenantToUser.getSecurity().setCreator(admin);
            tenantToUser=tenantService.merge(tenantToUser);
        }


        return new TenantAndUserInit(admin, defaultTenant,tenantToUser);
    }


    /**
     * creates all defaults instances, these are defined by the {@link AnnotatedClazz}
     */
    @ConditionalOnMissingBean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    public DefaultSecurityEntities createDefaultObjects(Clazzes clazzes, TenantAndUserInit tenantAndUserInit, DefaultRoleProvider defaultRoleProvider,
                                                        DefaultRoleToUserProvider defaultRoleToUserProvider, RoleToUserService roleToUserService,RoleService roleService) {
        Tenant defaultTenant = tenantAndUserInit.getDefaultTenant();
        User admin = tenantAndUserInit.getAdmin();
        TenantToUser tenantToUser=tenantAndUserInit.getTenantToUser();

        RoleCreate roleCreate = new RoleCreate()
                .setTenant(defaultTenant)
                .setName("Super Administrators")
                .setDescription("Role for Super Administrators of the system")
                .setIdForCreate(SUPER_ADMIN_ROLE_ID);
        Role superAdminRole = roleService.findByIdOrNull(Role.class, SUPER_ADMIN_ROLE_ID);
        if (superAdminRole == null) {
            logger.debug("Creating Super Admin role");
            superAdminRole = defaultRoleProvider.createRole(roleCreate);
            superAdminRole.getSecurity().setTenant(defaultTenant);
            superAdminRole.getSecurity().setCreator(admin);
            superAdminRole = roleToUserService.merge(superAdminRole);
        }
        if(superAdminRole.getSecurity().getCreator()==null){
            superAdminRole.getSecurity().setCreator(admin);
            superAdminRole=roleService.merge(superAdminRole);
        }
        RoleToUserCreate roleToUserCreate = new RoleToUserCreate()
                .setRole(superAdminRole)
                .setUser(admin)
                .setIdForCreate(SUPER_ADMIN_TO_ADMIN_ID);
        RoleToUser roleToUser = roleToUserService.findByIdOrNull(RoleToUser.class, SUPER_ADMIN_TO_ADMIN_ID);
        if (roleToUser == null) {
            logger.debug("Creating Role To User Link");
            roleToUser = defaultRoleToUserProvider.createRoleToUser(roleToUserCreate);
        }

        return new DefaultSecurityEntities(tenantAndUserInit.getAdmin(), tenantAndUserInit.getDefaultTenant(), superAdminRole, tenantToUser, roleToUser);

    }


}
