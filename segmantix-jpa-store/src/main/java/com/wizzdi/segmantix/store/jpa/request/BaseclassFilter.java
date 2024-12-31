package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.Clazz;

import java.util.List;


public class BaseclassFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;
    private List<Clazz> clazzes;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends BaseclassFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends BaseclassFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }
}
