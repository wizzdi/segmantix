package com.wizzdi.segmantix.spring.request;

import java.util.Optional;

public class ClazzFilter extends PaginationFilter {
    private BasicPropertiesFilter basicPropertiesFilter;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends ClazzFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.ClazzFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.ClazzFilter clazzFilter=new com.wizzdi.segmantix.store.jpa.request.ClazzFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null));
        forService(clazzFilter);
        return clazzFilter;
    }
}
