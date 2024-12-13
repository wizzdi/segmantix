package com.wizzdi.segmantix.impl.model;

import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class OperationGroupLink extends Basic implements IOperationGroupLink {

    @ManyToOne(targetEntity = Operation.class)
    private Operation operation;
    @ManyToOne(targetEntity = OperationGroup.class)
    private OperationGroup operationGroup;


    @ManyToOne(targetEntity = Operation.class)
    public Operation getOperation() {
        return operation;
    }

    public <T extends OperationGroupLink> T setOperation(Operation operation) {
        this.operation = operation;
        return (T) this;
    }

    @ManyToOne(targetEntity = OperationGroup.class)
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends OperationGroupLink> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }
}
