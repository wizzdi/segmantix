package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.RoleToUser;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "roleToUser", fieldType = RoleToUser.class, field = "id", groups = {Update.class}),
})
public class RoleToUserUpdate extends RoleToUserCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private RoleToUser roleToUser;

    public String getId() {
        return id;
    }

    public <T extends RoleToUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RoleToUser getRoleToUser() {
        return roleToUser;
    }

    public <T extends RoleToUserUpdate> T setRoleToUser(RoleToUser roleToUser) {
        this.roleToUser = roleToUser;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.RoleToUserUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.RoleToUserUpdate roleToUserUpdate=new com.wizzdi.segmantix.store.jpa.request.RoleToUserUpdate()
                .setRoleToUser(roleToUser);
        forService(roleToUserUpdate);
        return roleToUserUpdate;
    }
}
