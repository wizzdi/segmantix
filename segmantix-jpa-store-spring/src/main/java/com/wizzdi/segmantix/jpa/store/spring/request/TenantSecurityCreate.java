package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "tenant", fieldType = Tenant.class, field = "tenantId", groups = {Update.class, Create.class}),
})
public class TenantSecurityCreate extends SecurityCreate {

    @JsonIgnore
    private Tenant tenant;
    private String tenantId;


    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends TenantSecurityCreate> T setTenant(Tenant baseclass) {
        this.tenant = baseclass;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends TenantSecurityCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
}
