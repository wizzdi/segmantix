package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.OperationGroup;

public class OperationGroupUpdate extends OperationGroupCreate {

    private OperationGroup operation;

    public OperationGroup getOperation() {
        return operation;
    }

    public <T extends OperationGroupUpdate> T setOperation(OperationGroup operation) {
        this.operation = operation;
        return (T) this;
    }
}
