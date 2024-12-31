package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.TenantToUser;
public class TenantToUserUpdate extends TenantToUserCreate {

    private TenantToUser tenantToUser;


    public TenantToUser getTenantToUser() {
        return tenantToUser;
    }

    public <T extends TenantToUserUpdate> T setTenantToUser(TenantToUser tenantToUser) {
        this.tenantToUser = tenantToUser;
        return (T) this;
    }
}
