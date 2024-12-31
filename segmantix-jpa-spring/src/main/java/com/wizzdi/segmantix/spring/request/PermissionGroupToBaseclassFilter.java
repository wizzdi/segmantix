package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.spring.validation.ClazzValid;
import com.wizzdi.segmantix.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds"),
})
public class PermissionGroupToBaseclassFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;


    @JsonAlias("baseclassIds")
    private Set<String> securedIds =new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    @ClazzValid
    private List<Clazz> clazzes;

    private Set<String> permissionGroupIds = new HashSet<>();

    private Sorting sorting;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }

    public Set<String> getPermissionGroupIds() {
        return permissionGroupIds;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroupIds(Set<String> permissionGroupIds) {
        this.permissionGroupIds = permissionGroupIds;
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

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter=new com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null))
                .setPermissionGroups(permissionGroups)
                .setSorting(Optional.ofNullable(sorting).map(f->f.forService()).orElse(null))
                .setClazzes(clazzes)
                .setSecuredIds(securedIds);
        super.forService(permissionGroupToBaseclassFilter);
        return permissionGroupToBaseclassFilter;
    }

    public record Sorting(SortBy sortBy, boolean asc){
        public com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.Sorting forService(){
            return new com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.Sorting(sortBy.forService(),asc);
        }
    }
    public enum SortBy{
        BASECLASS_ID,CLAZZ_NAME,BASECLASS_NAME,BASECLASS_CREATION_DATE;

        public com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.SortBy forService() {
            return switch (this){
                case BASECLASS_ID -> com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.SortBy.BASECLASS_ID;
                case CLAZZ_NAME -> com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.SortBy.CLAZZ_NAME;
                case BASECLASS_NAME -> com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.SortBy.BASECLASS_NAME;
                case BASECLASS_CREATION_DATE -> com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter.SortBy.BASECLASS_CREATION_DATE;
            };
        }
    }
}
