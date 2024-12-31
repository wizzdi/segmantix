package com.wizzdi.segmantix.spring.request;

public class SecurityUserFilter extends SecurityEntityFilter {
    public com.wizzdi.segmantix.store.jpa.request.SecurityUserFilter forService(){
        com.wizzdi.segmantix.store.jpa.request.SecurityUserFilter securityUserFilter=new com.wizzdi.segmantix.store.jpa.request.SecurityUserFilter();
        forService(securityUserFilter);
        return securityUserFilter;
    }
}
