package com.wizzdi.segmantix.spring.request;

public class PermissionGroupCreate extends BasicCreate {


    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends PermissionGroupCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.PermissionGroupCreate permissionGroupCreate) {
        permissionGroupCreate.setExternalId(externalId);
        super.forService(permissionGroupCreate);
    }

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.PermissionGroupCreate permissionGroupCreate=new com.wizzdi.segmantix.store.jpa.request.PermissionGroupCreate();
        forService(permissionGroupCreate);
        return permissionGroupCreate;
    }
}
