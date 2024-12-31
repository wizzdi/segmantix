package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds", groups = {Create.class, Update.class})


})
public class PermissionGroupToBaseclassMassCreate {

    private List<SecuredHolder> securedHolders = new ArrayList<>();
    @JsonIgnore
    private Set<String> permissionGroupIds = new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    public List<SecuredHolder> getSecuredHolders() {
        return securedHolders;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setSecuredHolders(List<SecuredHolder> securedHolders) {
        this.securedHolders = securedHolders;
        return (T) this;
    }

    public Set<String> getPermissionGroupIds() {
        return permissionGroupIds;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setPermissionGroupIds(Set<String> permissionGroupIds) {
        this.permissionGroupIds = permissionGroupIds;
        return (T) this;
    }

    @JsonIgnore
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
