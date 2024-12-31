package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
public class OperationToClazzCreate extends BasicCreate {

    private SecurityOperation securityOperation;
    private Clazz type;

    public SecurityOperation getSecurityOperation() {
        return securityOperation;
    }

    public <T extends OperationToClazzCreate> T setSecurityOperation(SecurityOperation securityOperation) {
        this.securityOperation = securityOperation;
        return (T) this;
    }



    public Clazz getType() {
        return type;
    }

    public <T extends OperationToClazzCreate> T setType(Clazz type) {
        this.type = type;
        return (T) this;
    }
}
