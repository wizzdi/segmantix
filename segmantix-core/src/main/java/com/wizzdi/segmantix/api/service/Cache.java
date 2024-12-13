package com.wizzdi.segmantix.api.service;

import java.util.concurrent.Callable;

public interface Cache {

    <T> T get(Object key, Class<T> type);
    void put(Object key, Object value);

    <T> T get(Object key, Callable<T> valueLoader);

}
