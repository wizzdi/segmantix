package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "users", fieldType = SecurityUser.class, field = "userIds")
})
public class SecurityTenantFilter extends SecurityEntityFilter {

    private Set<String> userIds=new HashSet<>();

    @JsonIgnore
    private List<SecurityUser> users;

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends SecurityTenantFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends SecurityTenantFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter forService(){
        com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter securityTenantFilter=new com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter()
                .setUsers(users);
        forService(securityTenantFilter);
        return securityTenantFilter;
    }
}
