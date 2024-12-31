package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "roleId", groups = {Create.class, Update.class}),

})
public class RoleToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private Role role;
    private String roleId;


    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleToBaseclassCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends RoleToBaseclassCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }


    public com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassCreate forService(){
        com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassCreate roleToBaseclassCreate=new com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassCreate();
        forService(roleToBaseclassCreate);
        return roleToBaseclassCreate;
    }
    protected void forService(com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassCreate roleToBaseclassCreate) {
        roleToBaseclassCreate.setRole(role);
        super.forService(roleToBaseclassCreate);
    }
}
