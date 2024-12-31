package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.spring.validation.ClazzValid;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

import java.time.OffsetDateTime;

@IdValid.List({
        @IdValid(targetField = "permissionGroup", fieldType = PermissionGroup.class, field = "permissionGroupId", groups = {Create.class, Update.class}),


})
public class PermissionGroupToBaseclassCreate extends BasicCreate {

    @JsonIgnore
    private PermissionGroup permissionGroup;
    private String permissionGroupId;

    @JsonAlias("baseclassId")
    private String securedId;
    @ClazzValid
    private Clazz securedType;
    @JsonIgnore
    private OffsetDateTime securedCreationDate;

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public String getPermissionGroupId() {
        return permissionGroupId;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setPermissionGroupId(String permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
        return (T) this;
    }

    public String getSecuredId() {
        return securedId;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setSecuredId(String securedId) {
        this.securedId = securedId;
        return (T) this;
    }

    public Clazz getSecuredType() {
        return securedType;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setSecuredType(Clazz securedType) {
        this.securedType = securedType;
        return (T) this;
    }

    @JsonIgnore
    public OffsetDateTime getSecuredCreationDate() {
        return securedCreationDate;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setSecuredCreationDate(OffsetDateTime securedCreationDate) {
        this.securedCreationDate = securedCreationDate;
        return (T) this;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate) {
        permissionGroupToBaseclassCreate
                .setPermissionGroup(permissionGroup)
                .setSecuredCreationDate(securedCreationDate)
                .setSecuredId(securedId)
                .setSecuredType(securedType);
        super.forService(permissionGroupToBaseclassCreate);
    }

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate=new com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassCreate();
        forService(permissionGroupToBaseclassCreate);
        return permissionGroupToBaseclassCreate;
    }
}
