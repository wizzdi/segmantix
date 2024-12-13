package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "roles", field = "roleIds",fieldType = Role.class)
})
public class RoleSecurityFilter extends SecurityFilter {

    @JsonIgnore
    private List<Role> roles;
    private Set<String> roleIds=new HashSet<>();

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleSecurityFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public <T extends RoleSecurityFilter> T setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
        return (T) this;
    }
}
