package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "roles", field = "roleIds",fieldType = Role.class)
})
public class RoleToBaseclassFilter extends SecurityLinkFilter {

    @JsonIgnore
    private List<Role> roles;
    private Set<String> roleIds=new HashSet<>();

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleToBaseclassFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public <T extends RoleToBaseclassFilter> T setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassFilter forService(){
        com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassFilter roleToBaseclassFilter=new com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassFilter()
                .setRoles(roles);
        super.forService(roleToBaseclassFilter);
        return roleToBaseclassFilter;
    }
}
