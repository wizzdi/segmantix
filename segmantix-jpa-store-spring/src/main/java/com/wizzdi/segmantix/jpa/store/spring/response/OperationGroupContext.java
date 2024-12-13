package com.wizzdi.segmantix.jpa.store.spring.response;

import com.flexicore.model.Operation;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupCreate;

import java.util.List;

public record OperationGroupContext(OperationGroupCreate operationGroupCreate,List<Operation> operations) {
}
