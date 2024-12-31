package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TenantToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;


    private List<SecurityUser> users;
    private List<SecurityTenant> tenants;


    
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends TenantToUserFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }


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
}
