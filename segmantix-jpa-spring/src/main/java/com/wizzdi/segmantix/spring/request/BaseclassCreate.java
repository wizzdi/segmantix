package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "tenant", fieldType = SecurityTenant.class, field = "tenantId", groups = {Create.class, Update.class})
})
public class BaseclassCreate extends BasicCreate {

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;
    private Boolean systemObject;


    public BaseclassCreate(BaseclassCreate other) {
        super(other);
        this.tenant = other.tenant;
        this.tenantId = other.tenantId;
        this.systemObject = other.systemObject;
    }

    public BaseclassCreate() {
    }

    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends BaseclassCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends BaseclassCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
