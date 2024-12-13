package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = User.class, field = "id", groups = {Update.class}),
})
public class UserUpdate extends UserCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private User user;

    public String getId() {
        return id;
    }

    public <T extends UserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends UserUpdate> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
