package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;

public class SecurityUserCreate extends SecurityEntityCreate {

    
    private SecurityTenant tenant;

    public SecurityUserCreate(SecurityUserCreate other) {
        super(other);
        this.tenant=other.tenant;
    }


    public SecurityUserCreate() {
    }

    
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends SecurityUserCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

}
