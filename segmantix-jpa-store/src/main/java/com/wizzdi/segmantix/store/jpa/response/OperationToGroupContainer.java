package com.wizzdi.segmantix.store.jpa.response;


import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

public record OperationToGroupContainer(String id, OperationGroup operationGroup, SecurityOperation securityOperation) {
}
