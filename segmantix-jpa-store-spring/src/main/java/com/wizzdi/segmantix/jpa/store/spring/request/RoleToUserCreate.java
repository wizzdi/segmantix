package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "roleId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "user", fieldType = User.class, field = "userId", groups = {Create.class, Update.class})


})
public class RoleToUserCreate extends BasicCreate {
    @JsonIgnore
    private Role role;
    private String roleId;
    @JsonIgnore
    private User user;
    private String userId;

    public RoleToUserCreate(RoleToUserCreate other) {
        super(other);
        this.role = other.role;
        this.roleId = other.roleId;
        this.user = other.user;
        this.userId = other.userId;
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
    public User getUser() {
        return user;
    }

    public <T extends RoleToUserCreate> T setUser(User user) {
        this.user = user;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends RoleToUserCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }
}
