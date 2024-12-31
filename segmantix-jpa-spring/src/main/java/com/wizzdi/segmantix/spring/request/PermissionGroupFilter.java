package com.wizzdi.segmantix.spring.request;

import java.util.Optional;
import java.util.Set;

public class PermissionGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> externalIds;

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends PermissionGroupFilter> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends PermissionGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.PermissionGroupFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.PermissionGroupFilter permissionGroupFilter=new com.wizzdi.segmantix.store.jpa.request.PermissionGroupFilter()
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f->f.forService()).orElse(null))
                .setExternalIds(externalIds);
        super.forService(permissionGroupFilter);
        return permissionGroupFilter;
    }
}
