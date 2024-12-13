package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "tenants", fieldType = Tenant.class, field = "tenantIds"),
        @IdValid(targetField = "users", fieldType = User.class, field = "userIds")

})
public class RoleFilter extends SecurityEntityFilter {

    private Set<String> tenantIds = new HashSet<>();
    @JsonIgnore
    private List<Tenant> tenants;

    private Set<String> userIds = new HashSet<>();
    @JsonIgnore
    private List<User> users;


    public Set<String> getTenantIds() {
        return tenantIds;
    }

    public <T extends RoleFilter> T setTenantIds(Set<String> tenantIds) {
        this.tenantIds = tenantIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getTenants() {
        return tenants;
    }

    public <T extends RoleFilter> T setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends RoleFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends RoleFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }
}
