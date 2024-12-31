package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
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
    public com.wizzdi.segmantix.store.jpa.request.RoleUpdate forService(){
        com.wizzdi.segmantix.store.jpa.request.RoleUpdate roleUpdate=new com.wizzdi.segmantix.store.jpa.request.RoleUpdate()
                .setRole(role);
        super.forService(roleUpdate);
        return roleUpdate;
    }
}
