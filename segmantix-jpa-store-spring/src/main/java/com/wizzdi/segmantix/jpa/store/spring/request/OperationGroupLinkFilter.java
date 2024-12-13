package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.OperationGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "operations", fieldType = Operation.class, field = "operationIds"),
        @IdValid(targetField = "operationGroups", fieldType = OperationGroup.class, field = "operationGroupIds")
})
public class OperationGroupLinkFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<Operation> operations;
    private Set<String> operationIds=new HashSet<>();

    @JsonIgnore
    private List<OperationGroup> operationGroups;
    private Set<String> operationGroupIds=new HashSet<>();

    @JsonIgnore
    public List<Operation> getOperations() {
        return operations;
    }

    public <T extends OperationGroupLinkFilter> T setOperations(List<Operation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public <T extends OperationGroupLinkFilter> T setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
        return (T) this;
    }

    @JsonIgnore
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends OperationGroupLinkFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    public Set<String> getOperationGroupIds() {
        return operationGroupIds;
    }

    public <T extends OperationGroupLinkFilter> T setOperationGroupIds(Set<String> operationGroupIds) {
        this.operationGroupIds = operationGroupIds;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationGroupLinkFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
