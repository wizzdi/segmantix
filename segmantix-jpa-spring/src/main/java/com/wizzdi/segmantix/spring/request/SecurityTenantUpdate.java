package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToUpdate", fieldType = SecurityTenant.class, field = "id", groups = {Update.class}),
})
public class SecurityTenantUpdate extends SecurityTenantCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityTenant tenantToUpdate;

    public String getId() {
        return id;
    }

    public <T extends SecurityTenantUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityTenant getTenantToUpdate() {
        return tenantToUpdate;
    }

    public <T extends SecurityTenantUpdate> T setTenantToUpdate(SecurityTenant tenantToUpdate) {
        this.tenantToUpdate = tenantToUpdate;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.SecurityTenantUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.SecurityTenantUpdate securityTenantUpdate=new com.wizzdi.segmantix.store.jpa.request.SecurityTenantUpdate()
                .setTenantToUpdate(tenantToUpdate);
        forService(securityTenantUpdate);
        return securityTenantUpdate;
    }
}
