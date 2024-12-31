package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;

public class TenantToUserCreate extends BasicCreate {

    private Boolean defaultTenant;
    
    private SecurityUser user;

    
    private SecurityTenant tenant;

    public TenantToUserCreate(TenantToUserCreate other) {
        super(other);
        this.defaultTenant = other.defaultTenant;
        this.user = other.user;
        this.tenant = other.tenant;
    }

    public TenantToUserCreate() {
    }

    public Boolean getDefaultTenant() {
        return defaultTenant;
    }

    public <T extends TenantToUserCreate> T setDefaultTenant(Boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
        return (T) this;
    }

    
    public SecurityUser getUser() {
        return user;
    }

    public <T extends TenantToUserCreate> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }


    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToUserCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

}
