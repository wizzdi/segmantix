package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;
public class SecurityLinkGroupUpdate extends SecurityLinkGroupCreate {


    private SecurityLinkGroup securityLinkGroup;


    
    public SecurityLinkGroup getSecurityLinkGroup() {
        return securityLinkGroup;
    }

    public <T extends SecurityLinkGroupUpdate> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
        this.securityLinkGroup = securityLinkGroup;
        return (T) this;
    }
}
