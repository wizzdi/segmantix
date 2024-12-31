package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import java.util.List;

public class SecurityTenantFilter extends SecurityEntityFilter {

    private List<SecurityUser> users;


    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends SecurityTenantFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }
}
