package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid(field = "tenantId",targetField = "tenant",fieldType = Tenant.class,groups = {Create.class, Update.class})
public class UserCreate extends SecurityEntityCreate {

    @JsonIgnore
    private Tenant tenant;
    private String tenantId;

    public UserCreate(UserCreate other) {
        super(other);
        this.tenant=other.tenant;
        this.tenantId=other.tenantId;
    }


    public UserCreate() {
    }

    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends UserCreate> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends UserCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
}
