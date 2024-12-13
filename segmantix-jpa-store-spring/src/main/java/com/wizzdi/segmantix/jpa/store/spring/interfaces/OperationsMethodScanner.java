package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.jpa.store.spring.response.OperationScanContext;

import java.lang.reflect.Method;

public interface OperationsMethodScanner {

	OperationScanContext scanOperationOnMethod(Method method);


}
