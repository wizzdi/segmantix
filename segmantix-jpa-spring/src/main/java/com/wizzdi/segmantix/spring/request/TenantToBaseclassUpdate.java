package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToBaseclass", fieldType = TenantToBaseclass.class, field = "id", groups = {Update.class}),
})
public class TenantToBaseclassUpdate extends TenantToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private TenantToBaseclass tenantToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends TenantToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public TenantToBaseclass getTenantToBaseclass() {
        return tenantToBaseclass;
    }

    public <T extends TenantToBaseclassUpdate> T setTenantToBaseclass(TenantToBaseclass tenantToBaseclass) {
        this.tenantToBaseclass = tenantToBaseclass;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassUpdate forService(){
        com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassUpdate tenantToBaseclassUpdate=new com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassUpdate()
                .setTenantToBaseclass(tenantToBaseclass);
        forService(tenantToBaseclassUpdate);
        return tenantToBaseclassUpdate;
    }
}
