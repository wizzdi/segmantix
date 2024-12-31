package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

import java.util.List;

public class OperationToClazzFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private List<SecurityOperation> securityOperations;
    private List<Clazz> clazzes;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToClazzFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public List<SecurityOperation> getSecurityOperations() {
        return securityOperations;
    }

    public <T extends OperationToClazzFilter> T setSecurityOperations(List<SecurityOperation> securityOperations) {
        this.securityOperations = securityOperations;
        return (T) this;
    }

    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends OperationToClazzFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }
}
