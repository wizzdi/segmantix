package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;

import java.util.List;

public class RoleToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private List<Role> roles;
    private List<SecurityUser> users;


    
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleToUserFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }


    
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends RoleToUserFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends RoleToUserFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
