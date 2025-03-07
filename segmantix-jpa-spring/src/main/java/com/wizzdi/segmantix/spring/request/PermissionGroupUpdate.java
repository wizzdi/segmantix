package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "permissionGroup", fieldType = PermissionGroup.class, field = "id", groups = {Update.class})
})
public class PermissionGroupUpdate extends PermissionGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private PermissionGroup permissionGroup;

    public String getId() {
        return id;
    }

    public <T extends PermissionGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupUpdate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.PermissionGroupUpdate permissionGroupUpdate=new com.wizzdi.segmantix.store.jpa.request.PermissionGroupUpdate()
                .setPermissionGroup(permissionGroup);
        super.forService(permissionGroupUpdate);
        return permissionGroupUpdate;
    }
}
