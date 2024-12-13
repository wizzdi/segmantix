package com.wizzdi.segmantix.jpa.store.spring.request;

public class InstanceGroupCreate extends BasicCreate {


    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends InstanceGroupCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }

}
