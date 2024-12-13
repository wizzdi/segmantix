package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "instanceGroups", fieldType = InstanceGroup.class, field = "instanceGroupIds"),
        @IdValid(targetField = "baseclasses", fieldType = Baseclass.class, field = "baseclassIds"),
        @IdValid(targetField = "clazzes", fieldType = Clazz.class, field = "clazzIds")
})
public class InstanceGroupLinkFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<Baseclass> baseclasses;
    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<InstanceGroup> instanceGroups;

    private Set<String> clazzIds=new HashSet<>();
    @JsonIgnore
    private List<Clazz> clazzes;

    private Set<String> instanceGroupIds = new HashSet<>();

    private Sorting sorting;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends InstanceGroupLinkFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<InstanceGroup> getInstanceGroups() {
        return instanceGroups;
    }

    public <T extends InstanceGroupLinkFilter> T setInstanceGroups(List<InstanceGroup> instanceGroups) {
        this.instanceGroups = instanceGroups;
        return (T) this;
    }

    public Set<String> getInstanceGroupIds() {
        return instanceGroupIds;
    }

    public <T extends InstanceGroupLinkFilter> T setInstanceGroupIds(Set<String> instanceGroupIds) {
        this.instanceGroupIds = instanceGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends InstanceGroupLinkFilter> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends InstanceGroupLinkFilter> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    public Set<String> getClazzIds() {
        return clazzIds;
    }

    public <T extends InstanceGroupLinkFilter> T setClazzIds(Set<String> clazzIds) {
        this.clazzIds = clazzIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends InstanceGroupLinkFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public <T extends InstanceGroupLinkFilter> T setSorting(Sorting sorting) {
        this.sorting = sorting;
        return (T) this;
    }

    public record Sorting(SortBy sortBy, boolean asc){}
    public enum SortBy{
        BASECLASS_ID,CLAZZ_NAME,BASECLASS_NAME,BASECLASS_CREATION_DATE
    }
}
