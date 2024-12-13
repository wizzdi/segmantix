package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.OperationGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid(targetField = "operationGroups",field = "operationGroupIds",fieldType = OperationGroup.class)
public class OperationFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> categories=new HashSet<>();

    @JsonIgnore
    private List<OperationGroup> operationGroups;

    private Set<String> operationGroupIds=new HashSet<>();
    private String categoryLike;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public <T extends OperationFilter> T setCategories(Set<String> categories) {
        this.categories = categories;
        return (T) this;
    }

    public String getCategoryLike() {
        return categoryLike;
    }

    public <T extends OperationFilter> T setCategoryLike(String categoryLike) {
        this.categoryLike = categoryLike;
        return (T) this;
    }

    @JsonIgnore
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends OperationFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    public Set<String> getOperationGroupIds() {
        return operationGroupIds;
    }

    public <T extends OperationFilter> T setOperationGroupIds(Set<String> operationGroupIds) {
        this.operationGroupIds = operationGroupIds;
        return (T) this;
    }
}
