package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = SecurityUser.class, field = "userId", groups = {Create.class, Update.class}),

})
public class UserToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private SecurityUser user;
    private String userId;


    @JsonIgnore
    public SecurityUser getUser() {
        return user;
    }

    public <T extends UserToBaseclassCreate> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends UserToBaseclassCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.UserToBaseclassCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.UserToBaseclassCreate userToBaseclassCreate=new com.wizzdi.segmantix.store.jpa.request.UserToBaseclassCreate();
        forService(userToBaseclassCreate);
        return userToBaseclassCreate;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.UserToBaseclassCreate userToBaseclassCreate) {
        userToBaseclassCreate.setUser(user);
        super.forService(userToBaseclassCreate);
    }
}
