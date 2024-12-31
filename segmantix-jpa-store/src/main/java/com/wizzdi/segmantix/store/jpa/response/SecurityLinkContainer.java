package com.wizzdi.segmantix.store.jpa.response;

import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityEntity;
import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

public record SecurityLinkContainer(String id, String securedId, String securedType, PermissionGroup permissionGroup,
                                    OperationGroup operationGroup, SecurityLinkGroup securityLinkGroup, Access access,
                                    SecurityOperation operation, SecurityEntity securityEntity) {
}
