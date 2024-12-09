package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.IOperationToGroup;
import com.wizzdi.segmantix.api.ISecurityOperation;
import com.wizzdi.segmantix.api.OperationToGroupService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationToGroupServiceImpl implements OperationToGroupService {
    @Override
    public List<IOperationToGroup> listAllOperationToGroups(List<ISecurityOperation> securityOperations) {
        return List.of();
    }
}
