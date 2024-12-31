package com.wizzdi.segmantix.stores.inmemory.service;

import com.wizzdi.segmantix.api.model.IOperation;
import org.springframework.stereotype.Component;


public class OperationService {
    public IOperation getAllOps(){
        return () -> "allOps";
    }
}
