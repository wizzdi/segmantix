package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleFilter extends SecurityEntityFilter {

    private List<SecurityTenant> tenants;

    private List<SecurityUser> users;



    
    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends RoleFilter> T setTenants(List<SecurityTenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends RoleFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }
}
