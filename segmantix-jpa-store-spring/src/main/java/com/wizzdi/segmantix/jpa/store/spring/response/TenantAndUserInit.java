package com.wizzdi.segmantix.jpa.store.spring.response;


import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.TenantToUser;
import com.wizzdi.segmantix.impl.model.User;

public class TenantAndUserInit {
    private final Tenant defaultTenant;
    private final User admin;
    private final TenantToUser tenantToUser;


    public TenantAndUserInit(User admin, Tenant defaultTenant, TenantToUser tenantToUser) {
        this.defaultTenant = defaultTenant;
        this.admin = admin;
        this.tenantToUser = tenantToUser;
    }

    public Tenant getDefaultTenant() {
        return defaultTenant;
    }

    public User getAdmin() {
        return admin;
    }

    public TenantToUser getTenantToUser() {
        return tenantToUser;
    }
}
