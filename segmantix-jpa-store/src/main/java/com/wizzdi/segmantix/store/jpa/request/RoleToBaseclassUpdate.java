package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass;

public class RoleToBaseclassUpdate extends RoleToBaseclassCreate {


    private RoleToBaseclass roleToBaseclass;


    public RoleToBaseclass getRoleToBaseclass() {
        return roleToBaseclass;
    }

    public <T extends RoleToBaseclassUpdate> T setRoleToBaseclass(RoleToBaseclass roleToBaseclass) {
        this.roleToBaseclass = roleToBaseclass;
        return (T) this;
    }
}
