package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityUser;

public class SecurityUserUpdate extends SecurityUserCreate {


    private SecurityUser securityUser;


    
    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public <T extends SecurityUserUpdate> T setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
        return (T) this;
    }
}
