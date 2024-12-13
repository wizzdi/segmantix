package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.OperationGroupLink;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = OperationGroupLink.class, field = "id", groups = {Update.class}),
})
public class OperationGroupLinkUpdate extends OperationGroupLinkCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private OperationGroupLink operationGroupLink;

    public String getId() {
        return id;
    }

    public <T extends OperationGroupLinkUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroupLink getOperationGroupLink() {
        return operationGroupLink;
    }

    public <T extends OperationGroupLinkUpdate> T setOperationGroupLink(OperationGroupLink operationGroupLink) {
        this.operationGroupLink = operationGroupLink;
        return (T) this;
    }
}
