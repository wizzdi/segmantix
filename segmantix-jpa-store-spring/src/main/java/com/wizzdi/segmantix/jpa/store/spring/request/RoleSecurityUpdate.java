package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.RoleSecurity;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "roleSecurity", fieldType = RoleSecurity.class, field = "id", groups = {Update.class})

})
public class RoleSecurityUpdate extends RoleSecurityCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private RoleSecurity roleSecurity;

    public String getId() {
        return id;
    }

    public <T extends RoleSecurityUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RoleSecurity getRoleSecurity() {
        return roleSecurity;
    }

    public <T extends RoleSecurityUpdate> T setRoleSecurity(RoleSecurity roleSecurity) {
        this.roleSecurity = roleSecurity;
        return (T) this;
    }
}
