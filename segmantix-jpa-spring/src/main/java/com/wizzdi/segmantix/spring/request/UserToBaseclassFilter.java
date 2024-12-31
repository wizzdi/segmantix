package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "users", field = "userIds",fieldType = SecurityUser.class)
})
public class UserToBaseclassFilter extends SecurityLinkFilter {

    @JsonIgnore
    private List<SecurityUser> users;
    private Set<String> userIds=new HashSet<>();

    @JsonIgnore
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends UserToBaseclassFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends UserToBaseclassFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.UserToBaseclassFilter forService(){
        com.wizzdi.segmantix.store.jpa.request.UserToBaseclassFilter userToBaseclassFilter=new com.wizzdi.segmantix.store.jpa.request.UserToBaseclassFilter()
                .setUsers(users);
        forService(userToBaseclassFilter);
        return userToBaseclassFilter;
    }
}
