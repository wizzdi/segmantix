package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "tenants", fieldType = Role.class, field = "tenantsIds"),
        @IdValid(targetField = "roles", fieldType = Tenant.class, field = "rolesIds"),

})
public class SecurityPolicyFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> tenantsIds = new HashSet<>();
    @JsonIgnore

    private List<Tenant> tenants;
    private Set<String> rolesIds = new HashSet<>();
    @JsonIgnore
    private List<Role> roles;
    private OffsetDateTime startTime;
    private Boolean enabled;
    private boolean includeNoRole;

    public Set<String> getTenantsIds() {
        return tenantsIds;
    }

    public <T extends SecurityPolicyFilter> T setTenantsIds(Set<String> tenantsIds) {
        this.tenantsIds = tenantsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getTenants() {
        return tenants;
    }

    public <T extends SecurityPolicyFilter> T setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    public Set<String> getRolesIds() {
        return rolesIds;
    }

    public <T extends SecurityPolicyFilter> T setRolesIds(Set<String> rolesIds) {
        this.rolesIds = rolesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends SecurityPolicyFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public <T extends SecurityPolicyFilter> T setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends SecurityPolicyFilter> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public boolean isIncludeNoRole() {
        return includeNoRole;
    }

    public <T extends SecurityPolicyFilter> T setIncludeNoRole(boolean includeNoRole) {
        this.includeNoRole = includeNoRole;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityPolicyFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
