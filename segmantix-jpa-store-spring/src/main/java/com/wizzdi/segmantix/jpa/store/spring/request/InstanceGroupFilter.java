package com.wizzdi.segmantix.jpa.store.spring.request;

import java.util.Set;

public class InstanceGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> externalIds;

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends InstanceGroupFilter> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends InstanceGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
