package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
public class RoleToUserCreate extends BasicCreate {
    
    private Role role;

    private SecurityUser securityUser;

    public RoleToUserCreate(RoleToUserCreate other) {
        super(other);
        this.role = other.role;
        this.securityUser = other.securityUser;
    }

    public RoleToUserCreate() {
    }

    
    public Role getRole() {
        return role;
    }

    public <T extends RoleToUserCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }


    
    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public <T extends RoleToUserCreate> T setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
        return (T) this;
    }

}
