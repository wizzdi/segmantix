package com.wizzdi.segmantix.stores.inmemory.service;

import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class OperationGroupLinkProviderImpl implements OperationGroupLinkProvider {
    private Map<String,LinkedBlockingQueue<IOperationGroupLink>> links=new ConcurrentHashMap<>();
    @Override
    public List<IOperationGroupLink> listAllOperationGroupLinks(List<IOperation> operations) {
        return List.of();
    }
}
