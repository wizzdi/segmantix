package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import jakarta.validation.constraints.NotEmpty;

@IdValid.List({
        @IdValid(targetField = "tenant", fieldType = Tenant.class, field = "tenantId", groups = {Create.class})
})
public class RoleCreate extends SecurityEntityCreate {

    @NotEmpty(groups = Create.class)
    private String tenantId;
    @JsonIgnore
    private Tenant tenant;

    public RoleCreate(RoleCreate other) {
        super(other);
        this.tenant=other.tenant;
        this.tenantId = other.tenantId;
    }

    public RoleCreate() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends RoleCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends RoleCreate> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }
}
