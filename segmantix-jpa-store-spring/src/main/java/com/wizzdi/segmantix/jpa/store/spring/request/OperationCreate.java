package com.wizzdi.segmantix.jpa.store.spring.request;


import com.wizzdi.segmantix.model.Access;

public class OperationCreate extends BasicCreate {

    private Access defaultAccess;

    private String category;

    public Access getDefaultAccess() {
        return defaultAccess;
    }

    public <T extends OperationCreate> T setDefaultAccess(Access defaultAccess) {
        this.defaultAccess = defaultAccess;
        return (T) this;
    }

    public String getCategory() {
        return category;
    }

    public <T extends OperationCreate> T setCategory(String category) {
        this.category = category;
        return (T) this;
    }
}
