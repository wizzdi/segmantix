package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = OperationToGroup.class, field = "id", groups = {Update.class}),
})
public class OperationToGroupUpdate extends OperationToGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private OperationToGroup operationToGroup;

    public String getId() {
        return id;
    }

    public <T extends OperationToGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public OperationToGroup getOperationToGroup() {
        return operationToGroup;
    }

    public <T extends OperationToGroupUpdate> T setOperationToGroup(OperationToGroup operationToGroup) {
        this.operationToGroup = operationToGroup;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.OperationToGroupUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.OperationToGroupUpdate operationToGroupUpdate=new com.wizzdi.segmantix.store.jpa.request.OperationToGroupUpdate()
                .setOperationToGroup(operationToGroup);
        super.forService(operationToGroupUpdate);
        return operationToGroupUpdate;
    }
}
