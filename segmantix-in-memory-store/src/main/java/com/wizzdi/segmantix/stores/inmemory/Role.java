package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ITenant;

public record Role(String id, Tenant tenant) implements IRole {

    @Override
    public ITenant getTenant() {
        return tenant();
    }

    @Override
    public String getId() {
        return id();
    }
}
