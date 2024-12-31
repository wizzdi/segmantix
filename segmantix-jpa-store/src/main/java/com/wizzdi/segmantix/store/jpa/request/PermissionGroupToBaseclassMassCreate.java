package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;

import java.util.ArrayList;
import java.util.List;

public class PermissionGroupToBaseclassMassCreate {

    private List<SecuredHolder> securedHolders = new ArrayList<>();
    private List<PermissionGroup> permissionGroups;

    public List<SecuredHolder> getSecuredHolders() {
        return securedHolders;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setSecuredHolders(List<SecuredHolder> securedHolders) {
        this.securedHolders = securedHolders;
        return (T) this;
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }
    public <T extends PermissionGroupToBaseclassMassCreate> T setBaseclasses(List<? extends Baseclass> list){
        this.setSecuredHolders(list.stream().map(f->new SecuredHolder(f.getSecurityId(), Clazz.ofClass(f.getClass()))).toList());
        return (T)this;
    }
}
