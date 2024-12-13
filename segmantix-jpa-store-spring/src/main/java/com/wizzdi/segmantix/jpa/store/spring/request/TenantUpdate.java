package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToUpdate", fieldType = Tenant.class, field = "id", groups = {Update.class}),
})
public class TenantUpdate extends TenantCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Tenant tenantToUpdate;

    public String getId() {
        return id;
    }

    public <T extends TenantUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getTenantToUpdate() {
        return tenantToUpdate;
    }

    public <T extends TenantUpdate> T setTenantToUpdate(Tenant tenantToUpdate) {
        this.tenantToUpdate = tenantToUpdate;
        return (T) this;
    }
}
