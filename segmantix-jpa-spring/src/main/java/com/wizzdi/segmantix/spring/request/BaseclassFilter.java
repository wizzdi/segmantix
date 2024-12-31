package com.wizzdi.segmantix.spring.request;

import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.spring.validation.ClazzValid;

import java.util.List;
import java.util.Optional;


public class BaseclassFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;
    @ClazzValid
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

    public com.wizzdi.segmantix.store.jpa.request.BaseclassFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.BaseclassFilter baseclassFilter=new com.wizzdi.segmantix.store.jpa.request.BaseclassFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null))
                .setClazzes(this.clazzes);
        super.forService(baseclassFilter);
        return baseclassFilter;
    }
}
