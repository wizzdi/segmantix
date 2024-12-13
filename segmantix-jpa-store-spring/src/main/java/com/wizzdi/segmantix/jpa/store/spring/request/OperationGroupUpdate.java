package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.OperationGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = OperationGroup.class, field = "id", groups = {Update.class}),
})
public class OperationGroupUpdate extends OperationGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private OperationGroup operation;

    public String getId() {
        return id;
    }

    public <T extends OperationGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroup getOperation() {
        return operation;
    }

    public <T extends OperationGroupUpdate> T setOperation(OperationGroup operation) {
        this.operation = operation;
        return (T) this;
    }
}
