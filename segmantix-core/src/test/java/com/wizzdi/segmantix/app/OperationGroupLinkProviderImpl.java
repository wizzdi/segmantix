package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import org.springframework.stereotype.Component;

import java.util.List;


public class OperationGroupLinkProviderImpl implements OperationGroupLinkProvider {
    @Override
    public List<IOperationGroupLink> listAllOperationGroupLinks(List<IOperation> operations) {
        return List.of();
    }
}
