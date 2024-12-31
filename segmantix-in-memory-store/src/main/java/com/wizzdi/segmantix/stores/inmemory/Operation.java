package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.IOperation;

public record Operation(String id) implements IOperation {

    @Override
    public String getId() {
        return id();
    }
}
