package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass;

public class UserToBaseclassUpdate extends UserToBaseclassCreate {

    private UserToBaseclass userToBaseclass;


    public UserToBaseclass getUserToBaseclass() {
        return userToBaseclass;
    }

    public <T extends UserToBaseclassUpdate> T setUserToBaseclass(UserToBaseclass userToBaseclass) {
        this.userToBaseclass = userToBaseclass;
        return (T) this;
    }
}
