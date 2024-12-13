package com.wizzdi.segmantix.api.service;


import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroupLink;

import java.util.List;

public interface OperationGroupLinkProvider {
    List<IOperationGroupLink> listAllOperationGroupLinks(List<IOperation> operations);
}
