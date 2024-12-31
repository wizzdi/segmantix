package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "permissionGroupToBaseclass", fieldType = PermissionGroupToBaseclass.class, field = "id", groups = {Update.class})
})
public class PermissionGroupToBaseclassUpdate extends PermissionGroupToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private PermissionGroupToBaseclass permissionGroupToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends PermissionGroupToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public PermissionGroupToBaseclass getPermissionGroupToBaseclass() {
        return permissionGroupToBaseclass;
    }

    public <T extends PermissionGroupToBaseclassUpdate> T setPermissionGroupToBaseclass(PermissionGroupToBaseclass permissionGroupToBaseclass) {
        this.permissionGroupToBaseclass = permissionGroupToBaseclass;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate=new com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassUpdate()
                .setPermissionGroupToBaseclass(permissionGroupToBaseclass);
        super.forService(permissionGroupToBaseclassUpdate);
        return permissionGroupToBaseclassUpdate;
    }
}
