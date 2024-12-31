package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate;

@IdValid(field = "tenantId",targetField = "tenant",fieldType = SecurityTenant.class,groups = {Create.class, Update.class})
public class SecurityUserCreate extends SecurityEntityCreate {

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;

    public SecurityUserCreate(SecurityUserCreate other) {
        super(other);
        this.tenant=other.tenant;
        this.tenantId=other.tenantId;
    }


    public SecurityUserCreate() {
    }

    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends SecurityUserCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends SecurityUserCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.SecurityUserCreate forService(){
        com.wizzdi.segmantix.store.jpa.request.SecurityUserCreate securityUserCreate=new com.wizzdi.segmantix.store.jpa.request.SecurityUserCreate();
        forService(securityUserCreate);
        return securityUserCreate;
    }
    protected void forService(com.wizzdi.segmantix.store.jpa.request.SecurityUserCreate securityUserCreate){
        securityUserCreate.setTenant(tenant);
        super.forService(securityUserCreate);
    }
}
