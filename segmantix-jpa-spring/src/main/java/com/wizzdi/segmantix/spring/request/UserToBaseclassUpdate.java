package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "userToBaseclass", fieldType = UserToBaseclass.class, field = "id", groups = {Update.class}),
})
public class UserToBaseclassUpdate extends UserToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private UserToBaseclass userToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends UserToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public UserToBaseclass getUserToBaseclass() {
        return userToBaseclass;
    }

    public <T extends UserToBaseclassUpdate> T setUserToBaseclass(UserToBaseclass userToBaseclass) {
        this.userToBaseclass = userToBaseclass;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.UserToBaseclassUpdate forService(){
        com.wizzdi.segmantix.store.jpa.request.UserToBaseclassUpdate userToBaseclassUpdate=new com.wizzdi.segmantix.store.jpa.request.UserToBaseclassUpdate()
                .setUserToBaseclass(userToBaseclass);
        forService(userToBaseclassUpdate);
        return userToBaseclassUpdate;
    }
}
