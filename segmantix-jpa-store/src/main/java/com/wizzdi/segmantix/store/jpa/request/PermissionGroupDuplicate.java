package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;

public class PermissionGroupDuplicate {

    private PermissionGroup permissionGroup;


    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupDuplicate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }
}
