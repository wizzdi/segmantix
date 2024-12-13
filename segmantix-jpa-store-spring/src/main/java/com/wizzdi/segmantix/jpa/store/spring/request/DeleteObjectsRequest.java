package com.wizzdi.segmantix.jpa.store.spring.request;

import java.util.List;

public class DeleteObjectsRequest {

    private List<DeleteObjectRequest> entries;

    public List<DeleteObjectRequest> getEntries() {
        return entries;
    }

    public <T extends DeleteObjectsRequest> T setEntries(List<DeleteObjectRequest> entries) {
        this.entries = entries;
        return (T) this;
    }
}
