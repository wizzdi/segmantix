package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Security;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "security", fieldType = Security.class, field = "id", groups = {Update.class}),
})
public class SecurityUpdate extends SecurityCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Security security;

    public String getId() {
        return id;
    }

    public <T extends SecurityUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Security getSecurity() {
        return security;
    }

    public <T extends SecurityUpdate> T setSecurity(Security security) {
        this.security = security;
        return (T) this;
    }
}
