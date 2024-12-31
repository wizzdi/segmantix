package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.TenantToUser;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToUser", fieldType = TenantToUser.class, field = "id", groups = {Update.class}),
})
public class TenantToUserUpdate extends TenantToUserCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private TenantToUser tenantToUser;

    public String getId() {
        return id;
    }

    public <T extends TenantToUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public TenantToUser getTenantToUser() {
        return tenantToUser;
    }

    public <T extends TenantToUserUpdate> T setTenantToUser(TenantToUser tenantToUser) {
        this.tenantToUser = tenantToUser;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.TenantToUserUpdate forService(){
        com.wizzdi.segmantix.store.jpa.request.TenantToUserUpdate tenantToUserUpdate=new com.wizzdi.segmantix.store.jpa.request.TenantToUserUpdate()
                .setTenantToUser(tenantToUser);
        forService(tenantToUserUpdate);
        return tenantToUserUpdate;
    }
}
