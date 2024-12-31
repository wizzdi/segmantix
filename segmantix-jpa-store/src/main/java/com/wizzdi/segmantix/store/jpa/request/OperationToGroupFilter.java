package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import java.util.List;

public class OperationToGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private List<SecurityOperation> operations;

    private List<OperationGroup> operationGroups;

    public List<SecurityOperation> getOperations() {
        return operations;
    }

    public <T extends OperationToGroupFilter> T setOperations(List<SecurityOperation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends OperationToGroupFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
