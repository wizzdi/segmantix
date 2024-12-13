package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = User.class, field = "userId", groups = {Update.class, Create.class}),
        @IdValid(targetField = "tenant", fieldType = Tenant.class, field = "tenantId", groups = {Update.class, Create.class})
})
public class TenantToUserCreate extends BasicCreate {

    private Boolean defaultTenant;
    @JsonIgnore
    private User user;
    private String userId;

    @JsonIgnore
    private Tenant tenant;
    private String tenantId;

    public TenantToUserCreate(TenantToUserCreate other) {
        super(other);
        this.defaultTenant = other.defaultTenant;
        this.user = other.user;
        this.userId = other.userId;
        this.tenant = other.tenant;
        this.tenantId=other.tenantId;
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
    public User getUser() {
        return user;
    }

    public <T extends TenantToUserCreate> T setUser(User user) {
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
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends TenantToUserCreate> T setTenant(Tenant tenant) {
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
}
