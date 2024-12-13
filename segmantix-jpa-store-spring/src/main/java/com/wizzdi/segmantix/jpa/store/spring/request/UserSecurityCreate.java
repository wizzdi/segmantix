package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = User.class, field = "userId", groups = {Create.class, Update.class}),

})
public class UserSecurityCreate extends SecurityCreate {

    @JsonIgnore
    private User user;
    private String userId;


    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends UserSecurityCreate> T setUser(User user) {
        this.user = user;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends UserSecurityCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

}
