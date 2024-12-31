package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.spring.annotations.TypeRetention;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "tenants", fieldType = SecurityTenant.class, field = "tenantIds"),
        @IdValid(targetField = "users", fieldType = SecurityUser.class, field = "userIds")

})
public class RoleFilter extends SecurityEntityFilter {

    private Set<String> tenantIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(SecurityTenant.class)
    private List<SecurityTenant> tenants;

    private Set<String> userIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(SecurityUser.class)
    private List<SecurityUser> users;


    public Set<String> getTenantIds() {
        return tenantIds;
    }

    public <T extends RoleFilter> T setTenantIds(Set<String> tenantIds) {
        this.tenantIds = tenantIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends RoleFilter> T setTenants(List<SecurityTenant> tenants) {
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
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends RoleFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.RoleFilter forService(){
        com.wizzdi.segmantix.store.jpa.request.RoleFilter roleFilter=new com.wizzdi.segmantix.store.jpa.request.RoleFilter()
                .setTenants(tenants)
                .setUsers(users);
        super.forService(roleFilter);
        return roleFilter;
    }
}
