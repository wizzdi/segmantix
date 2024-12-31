package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
public class UserToBaseclassCreate extends SecurityLinkCreate {

    
    private SecurityUser user;


    
    public SecurityUser getUser() {
        return user;
    }

    public <T extends UserToBaseclassCreate> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }


}
