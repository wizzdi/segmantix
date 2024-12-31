package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.spring.annotations.TypeRetention;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.spring.validation.ClazzValid;

import java.util.List;
import java.util.Optional;

public class OperationToClazzFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    @TypeRetention(SecurityOperation.class)
    private List<SecurityOperation> securityOperations;
    @ClazzValid
    @JsonIgnore
    @TypeRetention(Clazz.class)
    private List<Clazz> clazzes;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToClazzFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityOperation> getSecurityOperations() {
        return securityOperations;
    }

    public <T extends OperationToClazzFilter> T setSecurityOperations(List<SecurityOperation> securityOperations) {
        this.securityOperations = securityOperations;
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

    public com.wizzdi.segmantix.store.jpa.request.OperationToClazzFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.OperationToClazzFilter operationToClazzFilter=new com.wizzdi.segmantix.store.jpa.request.OperationToClazzFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null))
                .setSecurityOperations(securityOperations)
                .setClazzes(clazzes);
        super.forService(operationToClazzFilter);
        return operationToClazzFilter;
    }
}
