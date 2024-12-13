package com.wizzdi.segmantix.jpa.store.spring.request;

import jakarta.validation.Valid;


public class SecurityGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @Valid
    private SecurityFilter securityFilter;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public SecurityFilter getSecurityFilter() {
        return securityFilter;
    }

    public <T extends SecurityGroupFilter> T setSecurityFilter(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
        return (T) this;
    }
}
