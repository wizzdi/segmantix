package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({

        @IdValid(targetField = "tenants", field = "tenantIds",fieldType = SecurityTenant.class)
})
public class TenantToBaseclassFilter extends SecurityLinkFilter {

    private Set<String> tenantIds=new HashSet<>();

    @JsonIgnore
    private List<SecurityTenant> tenants;

    public Set<String> getTenantIds() {
        return tenantIds;
    }

    public <T extends TenantToBaseclassFilter> T setTenantIds(Set<String> tenantIds) {
        this.tenantIds = tenantIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends TenantToBaseclassFilter> T setTenants(List<SecurityTenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassFilter forService(){
        com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassFilter tenantToBaseclassFilter=new com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassFilter()
                .setTenants(tenants);
        forService(tenantToBaseclassFilter);
        return tenantToBaseclassFilter;
    }
}
