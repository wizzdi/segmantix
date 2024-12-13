package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "users", fieldType = User.class, field = "userIds"),
        @IdValid(targetField = "tenants", fieldType = Tenant.class, field = "tenantsIds"),

})
public class TenantToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> userIds = new HashSet<>();
    @JsonIgnore
    private List<User> users;
    private Set<String> tenantsIds = new HashSet<>();
    @JsonIgnore
    private List<Tenant> tenants;

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends TenantToUserFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends TenantToUserFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getTenantsIds() {
        return tenantsIds;
    }

    public <T extends TenantToUserFilter> T setTenantsIds(Set<String> tenantsIds) {
        this.tenantsIds = tenantsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getTenants() {
        return tenants;
    }

    public <T extends TenantToUserFilter> T setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends TenantToUserFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
