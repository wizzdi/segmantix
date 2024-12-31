package com.wizzdi.segmantix.spring.request;

public enum SecurityLinkOrder {
    USER,ROLE,TENANT;

    public com.wizzdi.segmantix.store.jpa.request.SecurityLinkOrder forService() {
        return switch (this){
            case USER -> com.wizzdi.segmantix.store.jpa.request.SecurityLinkOrder.USER;
            case ROLE -> com.wizzdi.segmantix.store.jpa.request.SecurityLinkOrder.ROLE;
            case TENANT -> com.wizzdi.segmantix.store.jpa.request.SecurityLinkOrder.TENANT;
        };
    }
}
