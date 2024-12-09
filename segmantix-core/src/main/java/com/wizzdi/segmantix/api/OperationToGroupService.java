package com.wizzdi.segmantix.api;


import java.util.List;

public interface OperationToGroupService {
    List<IOperationToGroup> listAllOperationToGroups(List<ISecurityOperation> securityOperations);
}
