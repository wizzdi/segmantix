package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass;

public class TenantToBaseclassUpdate extends TenantToBaseclassCreate {

    private String id;
    
    private TenantToBaseclass tenantToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends TenantToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    
    public TenantToBaseclass getTenantToBaseclass() {
        return tenantToBaseclass;
    }

    public <T extends TenantToBaseclassUpdate> T setTenantToBaseclass(TenantToBaseclass tenantToBaseclass) {
        this.tenantToBaseclass = tenantToBaseclass;
        return (T) this;
    }
}
