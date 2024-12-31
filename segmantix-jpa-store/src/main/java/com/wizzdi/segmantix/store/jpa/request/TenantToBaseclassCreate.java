package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
public class TenantToBaseclassCreate extends SecurityLinkCreate {

    
    private SecurityTenant tenant;


    
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToBaseclassCreate> T setTenant(SecurityTenant baseclass) {
        this.tenant = baseclass;
        return (T) this;
    }

}
