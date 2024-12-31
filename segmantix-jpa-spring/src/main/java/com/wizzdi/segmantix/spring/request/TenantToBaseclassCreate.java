package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "tenant", fieldType = SecurityTenant.class, field = "tenantId", groups = {Update.class, Create.class}),
})
public class TenantToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;


    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToBaseclassCreate> T setTenant(SecurityTenant baseclass) {
        this.tenant = baseclass;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends TenantToBaseclassCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassCreate securityLinkCreate = new com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassCreate();
        forService(securityLinkCreate);
        return securityLinkCreate;

    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassCreate securityLinkCreate) {
        securityLinkCreate.setTenant(tenant);
        super.forService(securityLinkCreate);
    }
}
