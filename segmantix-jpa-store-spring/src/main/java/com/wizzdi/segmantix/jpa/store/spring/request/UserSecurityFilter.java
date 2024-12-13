package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "users", field = "userIds",fieldType = User.class)
})
public class UserSecurityFilter extends SecurityFilter {

    @JsonIgnore
    private List<User> users;
    private Set<String> userIds=new HashSet<>();

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends UserSecurityFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends UserSecurityFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }
}
