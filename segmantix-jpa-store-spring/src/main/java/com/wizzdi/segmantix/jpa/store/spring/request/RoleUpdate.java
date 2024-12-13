package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "id", groups = {Update.class}),
})
public class RoleUpdate extends RoleCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Role role;

    public String getId() {
        return id;
    }

    public <T extends RoleUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleUpdate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }
}
