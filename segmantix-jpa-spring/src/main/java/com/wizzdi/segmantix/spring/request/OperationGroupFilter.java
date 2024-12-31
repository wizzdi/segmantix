package com.wizzdi.segmantix.spring.request;

import java.util.Optional;
import java.util.Set;

public class OperationGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> externalIds;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends OperationGroupFilter> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.OperationGroupFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.OperationGroupFilter operationGroupFilter=new com.wizzdi.segmantix.store.jpa.request.OperationGroupFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null))
                .setExternalIds(externalIds);
        super.forService(operationGroupFilter);
        return operationGroupFilter;
    }
}
