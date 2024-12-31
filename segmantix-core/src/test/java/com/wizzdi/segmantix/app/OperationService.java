package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.model.IOperation;
import org.springframework.stereotype.Component;


public class OperationService {
    public IOperation getAllOps(){
        return () -> "allOps";
    }
}
