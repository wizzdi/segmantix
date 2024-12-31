package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

public class SecurityLinkCreate extends BasicCreate {

    private SecurityLinkGroup securityLinkGroup;
    private String securedId;
    private PermissionGroup permissionGroup;
    private Clazz clazz;
    private SecurityOperation operation;
    private String operationGroupId;
    private OperationGroup operationGroup;
    private Access access;






    
    public SecurityOperation getOperation() {
        return operation;
    }

    public <T extends SecurityLinkCreate> T setOperation(SecurityOperation operation) {
        this.operation = operation;
        return (T) this;
    }

    public Access getAccess() {
        return access;
    }

    public <T extends SecurityLinkCreate> T setAccess(Access access) {
        this.access = access;
        return (T) this;
    }


    
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends SecurityLinkCreate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public <T extends SecurityLinkCreate> T setClazz(Clazz clazz) {
        this.clazz = clazz;
        return (T) this;
    }

    public String getOperationGroupId() {
        return operationGroupId;
    }

    public <T extends SecurityLinkCreate> T setOperationGroupId(String operationGroupId) {
        this.operationGroupId = operationGroupId;
        return (T) this;
    }

    
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends SecurityLinkCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

    
    public SecurityLinkGroup getSecurityLinkGroup() {
        return securityLinkGroup;
    }

    public <T extends SecurityLinkCreate> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
        this.securityLinkGroup = securityLinkGroup;
        return (T) this;
    }

    public String getSecuredId() {
        return securedId;
    }

    public <T extends SecurityLinkCreate> T setSecuredId(String securedId) {
        this.securedId = securedId;
        return (T) this;
    }


}
