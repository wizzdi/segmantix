package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.TenantSecurity;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantSecurity", fieldType = TenantSecurity.class, field = "id", groups = {Update.class}),
})
public class TenantSecurityUpdate extends TenantSecurityCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private TenantSecurity tenantSecurity;

    public String getId() {
        return id;
    }

    public <T extends TenantSecurityUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public TenantSecurity getTenantSecurity() {
        return tenantSecurity;
    }

    public <T extends TenantSecurityUpdate> T setTenantSecurity(TenantSecurity tenantSecurity) {
        this.tenantSecurity = tenantSecurity;
        return (T) this;
    }
}
