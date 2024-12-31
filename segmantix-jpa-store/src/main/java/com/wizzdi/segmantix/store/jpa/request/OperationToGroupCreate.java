package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

public class OperationToGroupCreate extends BasicCreate {


    private SecurityOperation operation;

    private OperationGroup operationGroup;


    public SecurityOperation getOperation() {
        return operation;
    }

    public <T extends OperationToGroupCreate> T setOperation(SecurityOperation operation) {
        this.operation = operation;
        return (T) this;
    }

    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends OperationToGroupCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

}
