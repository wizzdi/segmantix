package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.Operation;


import java.util.List;

public class OperationToClazzFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<Operation> operations;
    @JsonIgnore
    private List<Clazz> clazzes;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToClazzFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<Operation> getOperations() {
        return operations;
    }

    public <T extends OperationToClazzFilter> T setOperations(List<Operation> operations) {
        this.operations = operations;
        return (T) this;
    }

    @JsonIgnore
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends OperationToClazzFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }
}
