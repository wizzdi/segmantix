package com.wizzdi.segmantix.jpa.store.spring.interfaces;


import com.wizzdi.segmantix.jpa.store.spring.response.OperationScanContext;

import java.util.List;

public interface StandardOperationScanner  {

	List<OperationScanContext> getStandardOperations();
}
