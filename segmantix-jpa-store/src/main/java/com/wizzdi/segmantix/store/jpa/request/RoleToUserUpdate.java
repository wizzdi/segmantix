package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.RoleToUser;
public class RoleToUserUpdate extends RoleToUserCreate {


    private RoleToUser roleToUser;


    public RoleToUser getRoleToUser() {
        return roleToUser;
    }

    public <T extends RoleToUserUpdate> T setRoleToUser(RoleToUser roleToUser) {
        this.roleToUser = roleToUser;
        return (T) this;
    }
}
