package com.wizzdi.segmantix.store.jpa.interfaces;

public interface MergeListener {
    void onCreate(Object t);
    void onUpdate(Object t);
}
