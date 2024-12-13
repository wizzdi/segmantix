package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityPolicy", fieldType = SecurityPolicy.class, field = "id", groups = {Update.class}),
})
public class SecurityPolicyUpdate extends SecurityPolicyCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityPolicy securityPolicy;

    public String getId() {
        return id;
    }

    public <T extends SecurityPolicyUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityPolicy getSecurityPolicy() {
        return securityPolicy;
    }

    public <T extends SecurityPolicyUpdate> T setSecurityPolicy(SecurityPolicy SecurityPolicy) {
        this.securityPolicy = SecurityPolicy;
        return (T) this;
    }
}
