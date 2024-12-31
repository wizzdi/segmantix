package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.IUser;

public record User(String id) implements IUser {

    @Override
    public String getId() {
        return id();
    }
}
