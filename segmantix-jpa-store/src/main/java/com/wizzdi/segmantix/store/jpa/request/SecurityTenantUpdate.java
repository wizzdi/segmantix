package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;

public class SecurityTenantUpdate extends SecurityTenantCreate {

    private SecurityTenant tenantToUpdate;


    public SecurityTenant getTenantToUpdate() {
        return tenantToUpdate;
    }

    public <T extends SecurityTenantUpdate> T setTenantToUpdate(SecurityTenant tenantToUpdate) {
        this.tenantToUpdate = tenantToUpdate;
        return (T) this;
    }
}
