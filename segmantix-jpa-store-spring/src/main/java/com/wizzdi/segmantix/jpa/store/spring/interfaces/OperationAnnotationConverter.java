package com.wizzdi.segmantix.jpa.store.spring.interfaces;


import com.wizzdi.segmantix.api.model.IOperation;

import java.lang.reflect.Method;

public interface OperationAnnotationConverter {

    IOperation getIOperation(Method t);
}
