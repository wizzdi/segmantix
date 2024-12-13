package com.wizzdi.segmantix.jpa.store.spring.service;

import com.wizzdi.segmantix.jpa.store.spring.response.OperationGroupContext;
import com.wizzdi.segmantix.jpa.store.spring.response.Operations;

public interface OperationGroupProvider {

    OperationGroupContext getOperationGroupContext(Operations operations);
}
