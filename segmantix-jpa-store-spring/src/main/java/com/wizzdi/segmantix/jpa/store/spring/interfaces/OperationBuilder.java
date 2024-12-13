package com.wizzdi.segmantix.jpa.store.spring.interfaces;


import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.segmantix.jpa.store.spring.response.OperationScanContext;

import java.util.List;
import java.util.Map;

public interface OperationBuilder {

	Operation upsertOperationNoMerge(OperationScanContext e, Map<String, Operation> operationMap, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes, SecurityContext securityContext);

}
