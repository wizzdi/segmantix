package com.wizzdi.segmantix.spring.config;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

record CacheWrapper(Cache cache) implements com.wizzdi.segmantix.api.service.Cache {

    @Override
    public <T> T get(Object key, Class<T> type) {
        return this.cache.get(key, type);
    }

    @Override
    public void put(Object key, Object value) {
        this.cache.put(key, value);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return cache.get(key, valueLoader);
    }
}
