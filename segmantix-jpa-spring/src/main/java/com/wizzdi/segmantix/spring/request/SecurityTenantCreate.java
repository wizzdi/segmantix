package com.wizzdi.segmantix.spring.request;

public class SecurityTenantCreate extends SecurityEntityCreate {

    public SecurityTenantCreate(SecurityTenantCreate other) {
        super(other);
    }

    public SecurityTenantCreate() {
    }

    public com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate forService(){
        com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate securityTenantCreate=new com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate();
        forService(securityTenantCreate);
        return securityTenantCreate;
    }

    public void forService(com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate securityTenantCreate) {
        super.forService(securityTenantCreate);
    }
}
