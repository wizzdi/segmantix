package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleToBaseclassFilter extends SecurityLinkFilter {

    
    private List<Role> roles;

    
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleToBaseclassFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }

}
