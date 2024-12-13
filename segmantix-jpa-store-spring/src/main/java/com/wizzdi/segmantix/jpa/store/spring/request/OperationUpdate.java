package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = Operation.class, field = "id", groups = {Update.class}),
})
public class OperationUpdate extends OperationCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Operation operation;

    public String getId() {
        return id;
    }

    public <T extends OperationUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public <T extends OperationUpdate> T setOperation(Operation operation) {
        this.operation = operation;
        return (T) this;
    }
}
