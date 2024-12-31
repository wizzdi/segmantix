package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.spring.validation.ClazzValid;
public class OperationToClazzCreate extends BasicCreate {

    @JsonIgnore
    private SecurityOperation securityOperation;
    @ClazzValid
    private Clazz type;

    @JsonIgnore
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

    public com.wizzdi.segmantix.store.jpa.request.OperationToClazzCreate forService() {
        com.wizzdi.segmantix.store.jpa.request.OperationToClazzCreate operationToClazzCreate=new com.wizzdi.segmantix.store.jpa.request.OperationToClazzCreate()
                .setSecurityOperation(securityOperation)
                .setType(type);
        super.forService(operationToClazzCreate);
        return operationToClazzCreate;
    }
}
