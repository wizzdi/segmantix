package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({

        @IdValid(targetField = "tenants", field = "tenantIds",fieldType = Tenant.class)
})
public class TenantSecurityFilter extends SecurityFilter {

    private Set<String> tenantIds=new HashSet<>();

    @JsonIgnore
    private List<Tenant> tenants;

    public Set<String> getTenantIds() {
        return tenantIds;
    }

    public <T extends TenantSecurityFilter> T setTenantIds(Set<String> tenantIds) {
        this.tenantIds = tenantIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getTenants() {
        return tenants;
    }

    public <T extends TenantSecurityFilter> T setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }
}
