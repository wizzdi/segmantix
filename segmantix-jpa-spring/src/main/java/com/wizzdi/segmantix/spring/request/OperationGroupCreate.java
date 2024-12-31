package com.wizzdi.segmantix.spring.request;

public class OperationGroupCreate extends BasicCreate {

    private String externalId;


    public String getExternalId() {
        return externalId;
    }

    public <T extends OperationGroupCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.OperationGroupCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.OperationGroupCreate operationGroupCreate=new com.wizzdi.segmantix.store.jpa.request.OperationGroupCreate();
        forService(operationGroupCreate);
        return operationGroupCreate;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.OperationGroupCreate operationGroupCreate) {
         operationGroupCreate.setExternalId(externalId);
        super.forService(operationGroupCreate);
    }
}
