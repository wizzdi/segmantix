package com.wizzdi.segmantix.spring.request;

import jakarta.validation.Valid;

import java.util.Optional;


public class SecurityLinkGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @Valid
    private SecurityLinkFilter securityLinkFilter;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityLinkGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public SecurityLinkFilter getSecurityLinkFilter() {
        return securityLinkFilter;
    }

    public <T extends SecurityLinkGroupFilter> T setSecurityLinkFilter(SecurityLinkFilter securityLinkFilter) {
        this.securityLinkFilter = securityLinkFilter;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupFilter securityLinkGroupFilter=new com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupFilter()
                .setSecurityLinkFilter(securityLinkFilter.forService())
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null));
        forService(securityLinkGroupFilter);
        return securityLinkGroupFilter;
    }
}
