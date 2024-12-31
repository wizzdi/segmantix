package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "roleToBaseclass", fieldType = RoleToBaseclass.class, field = "id", groups = {Update.class})

})
public class RoleToBaseclassUpdate extends RoleToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private RoleToBaseclass roleToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends RoleToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RoleToBaseclass getRoleToBaseclass() {
        return roleToBaseclass;
    }

    public <T extends RoleToBaseclassUpdate> T setRoleToBaseclass(RoleToBaseclass roleToBaseclass) {
        this.roleToBaseclass = roleToBaseclass;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassUpdate roleToBaseclassUpdate=new com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassUpdate()
                .setRoleToBaseclass(roleToBaseclass);
        forService(roleToBaseclassUpdate);
        return roleToBaseclassUpdate;
    }
}
