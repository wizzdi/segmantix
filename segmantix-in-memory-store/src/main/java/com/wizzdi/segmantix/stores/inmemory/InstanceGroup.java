package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.IInstanceGroup;

public record InstanceGroup(String id) implements IInstanceGroup {
    @Override
    public String getId() {
        return id;
    }
}
