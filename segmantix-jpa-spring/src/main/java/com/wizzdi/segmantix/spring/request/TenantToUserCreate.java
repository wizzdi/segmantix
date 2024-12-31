package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = SecurityUser.class, field = "userId", groups = {Update.class, Create.class}),
        @IdValid(targetField = "tenant", fieldType = SecurityTenant.class, field = "tenantId", groups = {Update.class, Create.class})
})
public class TenantToUserCreate extends BasicCreate {

    private Boolean defaultTenant;
    @JsonIgnore
    private SecurityUser user;
    private String userId;

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;

    public TenantToUserCreate(TenantToUserCreate other) {
        super(other);
        this.defaultTenant = other.defaultTenant;
        this.user = other.user;
        this.userId = other.userId;
        this.tenant = other.tenant;
        this.tenantId = other.tenantId;
    }

    public TenantToUserCreate() {
    }

    public Boolean getDefaultTenant() {
        return defaultTenant;
    }

    public <T extends TenantToUserCreate> T setDefaultTenant(Boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
        return (T) this;
    }

    @JsonIgnore
    public SecurityUser getUser() {
        return user;
    }

    public <T extends TenantToUserCreate> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends TenantToUserCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToUserCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends TenantToUserCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.TenantToUserCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.TenantToUserCreate tenantToUserCreate = new com.wizzdi.segmantix.store.jpa.request.TenantToUserCreate();
        forService(tenantToUserCreate);
        return tenantToUserCreate;

    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.TenantToUserCreate tenantToUserCreate) {
        tenantToUserCreate.setDefaultTenant(defaultTenant)
                .setTenant(tenant)
                .setUser(user);
        super.forService(tenantToUserCreate);
    }
}
