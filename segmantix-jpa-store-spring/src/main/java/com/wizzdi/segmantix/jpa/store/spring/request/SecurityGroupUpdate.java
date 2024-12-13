package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.SecurityGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityGroup", fieldType = SecurityGroup.class, field = "id", groups = {Update.class}),
})
public class SecurityGroupUpdate extends SecurityGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityGroup securityGroup;

    public String getId() {
        return id;
    }

    public <T extends SecurityGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityGroup getSecurityGroup() {
        return securityGroup;
    }

    public <T extends SecurityGroupUpdate> T setSecurityGroup(SecurityGroup securityGroup) {
        this.securityGroup = securityGroup;
        return (T) this;
    }
}
