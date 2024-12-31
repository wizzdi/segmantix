package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.service.Cache;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


public class CacheImpl implements Cache {
    private final ConcurrentHashMap<Object,Object> map=new ConcurrentHashMap<>();
    @Override
    public <T> T get(Object key, Class<T> type) {
        return (T) map.get(key);
    }

    @Override
    public void put(Object key, Object value) {

        map.put(key,value);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return (T) map.computeIfAbsent(key, f-> {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void invalidateAll(){
        map.clear();
    }
}
