package com.wizzdi.segmantix.spring.request;


public class SecurityLinkGroupCreate extends BasicCreate {


    public com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupCreate securityLinkGroupCreate=new com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupCreate();
        forService(securityLinkGroupCreate);
        return securityLinkGroupCreate;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupCreate securityLinkGroupCreate) {
        super.forService(securityLinkGroupCreate);
    }
}
