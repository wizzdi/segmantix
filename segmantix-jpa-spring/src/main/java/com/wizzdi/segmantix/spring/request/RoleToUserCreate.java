package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "roleId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "securityUser", fieldType = SecurityUser.class, field = "securityUserId", groups = {Create.class, Update.class})


})
public class RoleToUserCreate extends BasicCreate {
    @JsonIgnore
    private Role role;
    private String roleId;
    @JsonIgnore
    private SecurityUser securityUser;
    private String securityUserId;

    public RoleToUserCreate(RoleToUserCreate other) {
        super(other);
        this.role = other.role;
        this.roleId = other.roleId;
        this.securityUser = other.securityUser;
        this.securityUserId = other.securityUserId;
    }

    public RoleToUserCreate() {
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleToUserCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends RoleToUserCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public <T extends RoleToUserCreate> T setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
        return (T) this;
    }

    public String getSecurityUserId() {
        return securityUserId;
    }

    public <T extends RoleToUserCreate> T setSecurityUserId(String securityUserId) {
        this.securityUserId = securityUserId;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.RoleToUserCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.RoleToUserCreate roleToUserCreate=new com.wizzdi.segmantix.store.jpa.request.RoleToUserCreate();
        forService(roleToUserCreate);
        return roleToUserCreate;
    }
    protected void forService(com.wizzdi.segmantix.store.jpa.request.RoleToUserCreate roleToUserCreate){
        roleToUserCreate.setRole(role).setSecurityUser(securityUser);
        super.forService(roleToUserCreate);
    }
}
