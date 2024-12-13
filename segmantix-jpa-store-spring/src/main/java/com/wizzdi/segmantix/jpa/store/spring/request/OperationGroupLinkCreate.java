package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.OperationGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = Operation.class, field = "operationId"),
        @IdValid(targetField = "operationGroup", fieldType = OperationGroup.class, field = "operationGroupId")
})
public class OperationGroupLinkCreate extends BasicCreate {


    @JsonIgnore
    private Operation operation;
    private String operationId;

    @JsonIgnore
    private OperationGroup operationGroup;
    private String operationGroupId;


    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public <T extends OperationGroupLinkCreate> T setOperation(Operation operation) {
        this.operation = operation;
        return (T) this;
    }

    public String getOperationId() {
        return operationId;
    }

    public <T extends OperationGroupLinkCreate> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends OperationGroupLinkCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

    public String getOperationGroupId() {
        return operationGroupId;
    }

    public <T extends OperationGroupLinkCreate> T setOperationGroupId(String operationGroupId) {
        this.operationGroupId = operationGroupId;
        return (T) this;
    }
}
