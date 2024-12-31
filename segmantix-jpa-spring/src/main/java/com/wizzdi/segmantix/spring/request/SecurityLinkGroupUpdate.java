package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityLinkGroup", fieldType = SecurityLinkGroup.class, field = "id", groups = {Update.class}),
})
public class SecurityLinkGroupUpdate extends SecurityLinkGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityLinkGroup securityLinkGroup;

    public String getId() {
        return id;
    }

    public <T extends SecurityLinkGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityLinkGroup getSecurityLinkGroup() {
        return securityLinkGroup;
    }

    public <T extends SecurityLinkGroupUpdate> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
        this.securityLinkGroup = securityLinkGroup;
        return (T) this;
    }
    public com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupUpdate forService(){
        com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupUpdate securityLinkGroupUpdate=new com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupUpdate()
                .setSecurityLinkGroup(securityLinkGroup);
        forService(securityLinkGroupUpdate);
        return securityLinkGroupUpdate;
    }
}
