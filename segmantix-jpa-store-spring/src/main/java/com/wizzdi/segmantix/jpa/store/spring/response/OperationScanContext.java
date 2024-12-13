package com.wizzdi.segmantix.jpa.store.spring.response;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationCreate;

public class OperationScanContext {
	private final OperationCreate operationCreate;
	private final Class<?>[] relatedClasses;

	public OperationScanContext(OperationCreate operationCreate, Class<?>[] relatedClasses) {
		this.operationCreate = operationCreate;
		this.relatedClasses = relatedClasses;
	}


	public OperationCreate getOperationCreate() {
		return operationCreate;
	}

	public Class<?>[] getRelatedClasses() {
		return relatedClasses;
	}
}
