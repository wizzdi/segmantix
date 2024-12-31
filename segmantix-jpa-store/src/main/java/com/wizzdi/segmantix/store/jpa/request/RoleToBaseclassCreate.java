package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.Role;
public class RoleToBaseclassCreate extends SecurityLinkCreate {

    
    private Role role;


    
    public Role getRole() {
        return role;
    }

    public <T extends RoleToBaseclassCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }



}
