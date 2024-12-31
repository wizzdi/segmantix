package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import java.util.List;

public class TenantToBaseclassFilter extends SecurityLinkFilter {

    private List<SecurityTenant> tenants;


    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends TenantToBaseclassFilter> T setTenants(List<SecurityTenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }
}
