package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

import java.util.Collections;
import java.util.List;

public record Operations(List<SecurityOperation> operations,SecurityOperation allOperations) {
    public static Operations empty(SecurityOperation allOperations){
        return new Operations(Collections.emptyList(),allOperations);
    }
}
