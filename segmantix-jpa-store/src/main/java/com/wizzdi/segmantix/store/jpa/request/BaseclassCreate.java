package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;

public class BaseclassCreate extends BasicCreate {

    private SecurityTenant tenant;
    private Boolean systemObject;


    public BaseclassCreate(BaseclassCreate other) {
        super(other);
        this.tenant = other.tenant;
        this.systemObject = other.systemObject;
    }

    public BaseclassCreate() {
    }

    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends BaseclassCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }


    public Boolean getSystemObject() {
        return systemObject;
    }

    public <T extends BaseclassCreate> T setSystemObject(Boolean systemObject) {
        this.systemObject = systemObject;
        return (T) this;
    }
}
