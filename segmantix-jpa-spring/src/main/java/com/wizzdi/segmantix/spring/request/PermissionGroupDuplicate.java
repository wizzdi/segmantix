package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.spring.validation.IdValid;

@IdValid(targetField = "permissionGroup",field = "id",fieldType = PermissionGroup.class)
public class PermissionGroupDuplicate {

    private String id;

    @JsonIgnore
    private PermissionGroup permissionGroup;

    public String getId() {
        return id;
    }

    public <T extends PermissionGroupDuplicate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupDuplicate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupDuplicate forService() {
        return new com.wizzdi.segmantix.store.jpa.request.PermissionGroupDuplicate()
                .setPermissionGroup(permissionGroup);
    }
}
