package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.Role;
public class RoleUpdate extends RoleCreate {


    private Role role;


    public Role getRole() {
        return role;
    }

    public <T extends RoleUpdate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }
}
