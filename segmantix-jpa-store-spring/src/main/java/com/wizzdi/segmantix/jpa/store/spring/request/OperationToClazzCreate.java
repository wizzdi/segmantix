package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = Operation.class, field = "operationId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "clazz", fieldType = Clazz.class, field = "clazzId", groups = {Create.class, Update.class})

})
public class OperationToClazzCreate extends BasicCreate {

    @JsonIgnore
    private Operation operation;
    private String operationId;
    @JsonIgnore
    private Clazz clazz;
    private String clazzId;

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public <T extends OperationToClazzCreate> T setOperation(Operation operation) {
        this.operation = operation;
        return (T) this;
    }

    public String getOperationId() {
        return operationId;
    }

    public <T extends OperationToClazzCreate> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @JsonIgnore
    public Clazz getClazz() {
        return clazz;
    }

    public <T extends OperationToClazzCreate> T setClazz(Clazz clazz) {
        this.clazz = clazz;
        return (T) this;
    }

    public String getClazzId() {
        return clazzId;
    }

    public <T extends OperationToClazzCreate> T setClazzId(String clazzId) {
        this.clazzId = clazzId;
        return (T) this;
    }
}
