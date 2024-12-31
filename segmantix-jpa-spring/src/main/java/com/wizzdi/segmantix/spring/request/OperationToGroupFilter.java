package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.OperationValid;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "operationGroups", fieldType = OperationGroup.class, field = "operationGroupIds")
})
@OperationValid(targetField = "operations",sourceField = "operationIds")
public class OperationToGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<SecurityOperation> operations;
    private Set<String> operationIds=new HashSet<>();

    @JsonIgnore
    private List<OperationGroup> operationGroups;
    private Set<String> operationGroupIds=new HashSet<>();

    @JsonIgnore
    public List<SecurityOperation> getOperations() {
        return operations;
    }

    public <T extends OperationToGroupFilter> T setOperations(List<SecurityOperation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public <T extends OperationToGroupFilter> T setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
        return (T) this;
    }

    @JsonIgnore
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends OperationToGroupFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    public Set<String> getOperationGroupIds() {
        return operationGroupIds;
    }

    public <T extends OperationToGroupFilter> T setOperationGroupIds(Set<String> operationGroupIds) {
        this.operationGroupIds = operationGroupIds;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.OperationToGroupFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.OperationToGroupFilter operationToGroupFilter=new com.wizzdi.segmantix.store.jpa.request.OperationToGroupFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null))
                .setOperationGroups(operationGroups)
                .setOperations(operations);
        super.forService(operationToGroupFilter);
        return operationToGroupFilter;
    }
}
