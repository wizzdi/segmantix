package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;

import java.time.OffsetDateTime;

public class PermissionGroupToBaseclassCreate extends BasicCreate {

    private PermissionGroup permissionGroup;

    private String securedId;
    private Clazz securedType;
    private OffsetDateTime securedCreationDate;

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
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

    public OffsetDateTime getSecuredCreationDate() {
        return securedCreationDate;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setSecuredCreationDate(OffsetDateTime securedCreationDate) {
        this.securedCreationDate = securedCreationDate;
        return (T) this;
    }
}
