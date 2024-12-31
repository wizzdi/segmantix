package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;

public class PermissionGroupToBaseclassUpdate extends PermissionGroupToBaseclassCreate {


    private PermissionGroupToBaseclass permissionGroupToBaseclass;


    public PermissionGroupToBaseclass getPermissionGroupToBaseclass() {
        return permissionGroupToBaseclass;
    }

    public <T extends PermissionGroupToBaseclassUpdate> T setPermissionGroupToBaseclass(PermissionGroupToBaseclass permissionGroupToBaseclass) {
        this.permissionGroupToBaseclass = permissionGroupToBaseclass;
        return (T) this;
    }
}
