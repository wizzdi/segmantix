package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.model.Operation;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.model.TenantSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.SecurityApiRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Lazy
@Service

public class OperationValidatorService  {

    private static final Logger logger = LoggerFactory.getLogger(OperationValidatorService.class);

    String USER_TYPE = "USER";
    String ROLE_TYPE = "ROLE";
    String TENANT_TYPE = "TENANT";


    @Autowired
    private SecurityApiRepository securityApiRepository;
    @Autowired
    private TenantSecurityService tenantSecurityService;



    @Cacheable(value = "operationValidatorCache", key = "#key",cacheManager = "operationAccessControlCacheManager",unless = "#result==null")
    public Boolean getIsAllowedFromCache(String key) {
        return null;
    }

    @CachePut(value = "operationValidatorCache", key = "#key",cacheManager = "operationAccessControlCacheManager",unless = "#result==null")
    public Boolean updateIsAllowedCache(String key, Boolean value) {
        return value;
    }

    static String getAccessControlKey(String type, String opId, String securityEntityId, IOperation.Access access){
        return type+"."+opId+"."+securityEntityId+"."+access.name();
    }

    public boolean checkIfAllowed(SecurityContext securityContext) {
        Access defaultaccess = securityContext.getOperation() != null && securityContext.getOperation().getDefaultAccess() != null ? securityContext.getOperation().getDefaultAccess() : Access.allow;
        return checkIfAllowed(securityContext.getUser(), securityContext.getTenants(), securityContext.getOperation(), defaultaccess);
    }

    public boolean userAllowed(Operation operation, User user) {
        return checkUser(operation, user, IOperation.Access.allow);

    }

    public boolean userDenied(Operation operation, User user) {
        return checkUser(operation, user, IOperation.Access.deny);
    }

    public boolean roleAllowed(Operation operation, User user) {
        IOperation.Access access = IOperation.Access.allow;
        String cacheKey= getAccessControlKey(ROLE_TYPE,operation.getId(),user.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityApiRepository.checkRole(operation, user, access);
        updateIsAllowedCache(cacheKey,val);

        return val;

    }

    public boolean roleDenied(Operation operation, User user) {
        IOperation.Access access = IOperation.Access.deny;

        String cacheKey=getAccessControlKey(ROLE_TYPE,operation.getId(),user.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityApiRepository.checkRole(operation, user, access);
        updateIsAllowedCache(cacheKey,val);
        return val;
    }

    public boolean checkUser(Operation operation, User user, IOperation.Access access) {
        String cacheKey= getAccessControlKey(USER_TYPE,operation.getId(),user.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityApiRepository.checkUser(operation, user, access);
        updateIsAllowedCache(cacheKey,val);
        return val;
    }
    public boolean tenantAllowed(Operation operation, Tenant tenant) {
        IOperation.Access access=IOperation.Access.allow;
        String cacheKey= getAccessControlKey(TENANT_TYPE,operation.getId(),tenant.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        TenantSecurity link = tenantSecurityService.listAllTenantSecuritys(new TenantSecurityFilter().setTenants(Collections.singletonList(tenant)).setBaseclasses(Collections.singletonList(operation.getSecurity())).setAccesses(Collections.singleton(access)),null).stream().findFirst().orElse(null);
        val= link != null;
        updateIsAllowedCache(cacheKey,val);
        return val;
    }

    public boolean tenantDenied(Operation operation, Tenant tenant) {
        IOperation.Access access=IOperation.Access.deny;
        String cacheKey= getAccessControlKey(TENANT_TYPE,operation.getId(),tenant.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        TenantSecurity link = tenantSecurityService.listAllTenantSecuritys(new TenantSecurityFilter().setTenants(Collections.singletonList(tenant)).setBaseclasses(Collections.singletonList(operation.getSecurity())).setAccesses(Collections.singleton(access)),null).stream().findFirst().orElse(null);
        val= link != null;
        updateIsAllowedCache(cacheKey,val);
        return val;
    }


    
    public boolean checkIfAllowed(User user, List<Tenant> tenants, Operation operation, Access access) {

        if (userAllowed(operation, user)) {
            return true;
        } else {
            if (userDenied(operation, user)) {
                return false;
            }
            if (roleAllowed(operation, user)) {
                return true;
            } else {
                if (roleDenied(operation, user)) {

                    return false;
                } else {
                    for (Tenant tenant : tenants) {
                        if (tenantAllowed(operation, tenant)) {

                            return true;
                        }
                    }
                    boolean allDenied = true;
                    for (Tenant tenant : tenants) {
                        allDenied = tenantDenied(operation, tenant) && allDenied;

                    }
                    if (allDenied) {
                        return false;
                    } else {
                        return access == Access.allow;
                    }


                }
            }

        }
    }





}
