package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.SecurityLink;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityLink", fieldType = SecurityLink.class, field = "id", groups = {Update.class}),
})
public class SecurityLinkUpdate extends SecurityLinkCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityLink securityLink;

    public String getId() {
        return id;
    }

    public <T extends SecurityLinkUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityLink getSecurityLink() {
        return securityLink;
    }

    public <T extends SecurityLinkUpdate> T setSecurityLink(SecurityLink securityLink) {
        this.securityLink = securityLink;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.SecurityLinkUpdate forService() {
        com.wizzdi.segmantix.store.jpa.request.SecurityLinkUpdate securityLinkUpdate=new com.wizzdi.segmantix.store.jpa.request.SecurityLinkUpdate()
                .setSecurityLink(securityLink);
        forService(securityLinkUpdate);
        return securityLinkUpdate;
    }
}
