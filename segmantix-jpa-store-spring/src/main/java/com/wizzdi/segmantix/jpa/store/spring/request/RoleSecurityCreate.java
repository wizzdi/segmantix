package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "roleId", groups = {Create.class, Update.class}),

})
public class RoleSecurityCreate extends SecurityCreate {

    @JsonIgnore
    private Role role;
    private String roleId;


    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleSecurityCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends RoleSecurityCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }


}
