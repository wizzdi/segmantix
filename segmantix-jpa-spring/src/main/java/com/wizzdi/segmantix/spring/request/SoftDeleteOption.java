package com.wizzdi.segmantix.spring.request;

public enum SoftDeleteOption {
    DEFAULT, NON_DELETED_ONLY, DELETED_ONLY, BOTH;

    public com.wizzdi.segmantix.store.jpa.request.SoftDeleteOption forService() {
        return switch (this){
            case DEFAULT -> com.wizzdi.segmantix.store.jpa.request.SoftDeleteOption.DEFAULT;
            case NON_DELETED_ONLY -> com.wizzdi.segmantix.store.jpa.request.SoftDeleteOption.NON_DELETED_ONLY;
            case DELETED_ONLY -> com.wizzdi.segmantix.store.jpa.request.SoftDeleteOption.DELETED_ONLY;
            case BOTH -> com.wizzdi.segmantix.store.jpa.request.SoftDeleteOption.BOTH;
        };
    }
}
