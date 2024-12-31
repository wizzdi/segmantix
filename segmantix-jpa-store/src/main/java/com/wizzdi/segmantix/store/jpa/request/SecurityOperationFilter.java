package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.OperationGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecurityOperationFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> categories=new HashSet<>();

    
    private List<OperationGroup> operationGroups;

    private String categoryLike;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityOperationFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public <T extends SecurityOperationFilter> T setCategories(Set<String> categories) {
        this.categories = categories;
        return (T) this;
    }

    public String getCategoryLike() {
        return categoryLike;
    }

    public <T extends SecurityOperationFilter> T setCategoryLike(String categoryLike) {
        this.categoryLike = categoryLike;
        return (T) this;
    }

    
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends SecurityOperationFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

}
