package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionGroupToBaseclassFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;


    private Set<String> securedIds =new HashSet<>();
    
    private List<PermissionGroup> permissionGroups;

    private List<Clazz> clazzes;


    private Sorting sorting;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }




    public Set<String> getSecuredIds() {
        return securedIds;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setSecuredIds(Set<String> securedIds) {
        this.securedIds = securedIds;
        return (T) this;
    }

    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setSorting(Sorting sorting) {
        this.sorting = sorting;
        return (T) this;
    }

    public record Sorting(SortBy sortBy, boolean asc){}
    public enum SortBy{
        BASECLASS_ID,CLAZZ_NAME,BASECLASS_NAME,BASECLASS_CREATION_DATE
    }
}
