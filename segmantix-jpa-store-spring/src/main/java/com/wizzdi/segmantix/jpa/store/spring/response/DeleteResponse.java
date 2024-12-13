package com.wizzdi.segmantix.jpa.store.spring.response;

import com.wizzdi.segmantix.jpa.store.spring.request.DeleteObjectRequest;

import java.util.List;

public class DeleteResponse {

    private List<Object> deleted;
    private List<DeleteObjectRequest> failed;

    public List<Object> getDeleted() {
        return deleted;
    }

    public <T extends DeleteResponse> T setDeleted(List<Object> deleted) {
        this.deleted = deleted;
        return (T) this;
    }

    public List<DeleteObjectRequest> getFailed() {
        return failed;
    }

    public <T extends DeleteResponse> T setFailed(List<DeleteObjectRequest> failed) {
        this.failed = failed;
        return (T) this;
    }
}
