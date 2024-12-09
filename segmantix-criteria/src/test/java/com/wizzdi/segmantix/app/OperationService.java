package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.ISecurityOperation;
import org.springframework.stereotype.Component;

@Component
public class OperationService {
    public ISecurityOperation getAllOps(){
        return () -> "allOps";
    }
}
