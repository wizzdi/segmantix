package com.wizzdi.segmantix.spring.request;

import com.wizzdi.segmantix.store.jpa.request.PaginationFilter;

import java.util.Optional;

public class SecurityEntityFilter extends BaseclassFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends BaseclassFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.SecurityEntityFilter securityEntityFilter) {
        securityEntityFilter.setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null));
        super.forService(securityEntityFilter);
    }
}
