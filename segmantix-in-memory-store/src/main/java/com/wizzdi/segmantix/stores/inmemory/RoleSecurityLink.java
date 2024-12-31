package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroup;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.IRoleSecurityLink;
import com.wizzdi.segmantix.model.Access;

public record RoleSecurityLink(String id, String securedId, String securedType, IInstanceGroup instanceGroup,
                        Access access, IOperation operation, IOperationGroup operationGroup,
                        IRole role) implements IRoleSecurityLink {
    @Override
    public IRole getRole() {
        return role;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSecuredId() {
        return securedId;
    }

    @Override
    public String getSecuredType() {
        return securedType;
    }

    @Override
    public IInstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    @Override
    public Access getAccess() {
        return access;
    }

    @Override
    public IOperation getOperation() {
        return operation;
    }

    @Override
    public IOperationGroup getOperationGroup() {
        return operationGroup;
    }
}
