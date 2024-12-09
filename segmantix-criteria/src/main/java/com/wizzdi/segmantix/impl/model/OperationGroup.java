package com.wizzdi.segmantix.impl.model;

import com.wizzdi.segmantix.api.IOperationGroup;
import jakarta.persistence.Entity;

@Entity
public class OperationGroup extends Baseclass implements IOperationGroup {

    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends OperationGroup> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }
}
