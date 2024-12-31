package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityLink;
public class SecurityLinkUpdate extends SecurityLinkCreate {


    private SecurityLink securityLink;


    public SecurityLink getSecurityLink() {
        return securityLink;
    }

    public <T extends SecurityLinkUpdate> T setSecurityLink(SecurityLink securityLink) {
        this.securityLink = securityLink;
        return (T) this;
    }
}
