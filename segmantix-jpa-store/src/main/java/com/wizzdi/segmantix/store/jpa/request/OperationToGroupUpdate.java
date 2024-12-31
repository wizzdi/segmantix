package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;

public class OperationToGroupUpdate extends OperationToGroupCreate {

    private OperationToGroup operationToGroup;

    public OperationToGroup getOperationToGroup() {
        return operationToGroup;
    }

    public <T extends OperationToGroupUpdate> T setOperationToGroup(OperationToGroup operationToGroup) {
        this.operationToGroup = operationToGroup;
        return (T) this;
    }
}
