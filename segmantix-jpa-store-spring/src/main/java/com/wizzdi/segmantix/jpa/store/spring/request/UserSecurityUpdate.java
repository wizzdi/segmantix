package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.UserSecurity;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "userSecurity", fieldType = UserSecurity.class, field = "id", groups = {Update.class}),
})
public class UserSecurityUpdate extends UserSecurityCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private UserSecurity userSecurity;

    public String getId() {
        return id;
    }

    public <T extends UserSecurityUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public UserSecurity getUserSecurity() {
        return userSecurity;
    }

    public <T extends UserSecurityUpdate> T setUserSecurity(UserSecurity userSecurity) {
        this.userSecurity = userSecurity;
        return (T) this;
    }
}
