package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.spring.annotations.TypeRetention;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.IdValid;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "users", fieldType = SecurityUser.class, field = "userIds"),
        @IdValid(targetField = "tenants", fieldType = SecurityTenant.class, field = "tenantsIds"),

})
public class TenantToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> userIds = new HashSet<>();
    @TypeRetention(SecurityUser.class)
    @JsonIgnore
    private List<SecurityUser> users;
    private Set<String> tenantsIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(SecurityTenant.class)
    private List<SecurityTenant> tenants;

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends TenantToUserFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends TenantToUserFilter> T setUsers(List<SecurityUser> users) {
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
    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends TenantToUserFilter> T setTenants(List<SecurityTenant> tenants) {
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

    public com.wizzdi.segmantix.store.jpa.request.TenantToUserFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.TenantToUserFilter tenantToUserFilter=new com.wizzdi.segmantix.store.jpa.request.TenantToUserFilter()
                .setTenants(tenants)
                .setUsers(users)
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null));
        forService(tenantToUserFilter);
        return tenantToUserFilter;
    }
}
