package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "roles", fieldType = Role.class, field = "rolesIds"),
        @IdValid(targetField = "users", fieldType = User.class, field = "userIds")
})
public class RoleToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> rolesIds = new HashSet<>();
    @JsonIgnore
    private List<Role> roles;
    private Set<String> userIds = new HashSet<>();
    @JsonIgnore
    private List<User> users;

    public Set<String> getRolesIds() {
        return rolesIds;
    }

    public <T extends RoleToUserFilter> T setRolesIds(Set<String> rolesIds) {
        this.rolesIds = rolesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleToUserFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends RoleToUserFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends RoleToUserFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends RoleToUserFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
