package com.wizzdi.segmantix.jpa.store.spring.request;

public class ClazzFilter extends PaginationFilter {
    private BasicPropertiesFilter basicPropertiesFilter;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends ClazzFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
