package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.ITenant;

public record Tenant(String id) implements ITenant {

    @Override
    public String getId() {
        return id();
    }
}
