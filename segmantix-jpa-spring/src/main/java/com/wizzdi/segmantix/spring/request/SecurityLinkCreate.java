package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.spring.validation.ClazzValid;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.OperationValid;
import com.wizzdi.segmantix.spring.validation.Update;
import com.wizzdi.segmantix.model.Access;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityLinkGroup",field = "securityLinkGroupId",fieldType = SecurityLinkGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operationGroup",field = "operationGroupId",fieldType = OperationGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "permissionGroup",field = "permissionGroupId",fieldType = PermissionGroup.class, groups = {Create.class, Update.class}),
})
@OperationValid(targetField = "operation",sourceField = "operationId", groups = {Create.class, Update.class})
public class SecurityLinkCreate extends BasicCreate {

    private String securityLinkGroupId;
    @JsonIgnore
    private SecurityLinkGroup securityLinkGroup;



    @JsonAlias("baseclassId")
    private String securedId;

    @JsonIgnore
    private PermissionGroup permissionGroup;

    private String permissionGroupId;

    @ClazzValid
    @JsonProperty("clazz")
    private Clazz clazz;

    private String operationId;
    @JsonIgnore
    private SecurityOperation operation;

    private String operationGroupId;
    @JsonIgnore
    private OperationGroup operationGroup;

    @NotNull(groups = Create.class)
    private Access access;





    public String getOperationId() {
        return operationId;
    }

    public <T extends SecurityLinkCreate> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @JsonIgnore
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


    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends SecurityLinkCreate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public String getPermissionGroupId() {
        return permissionGroupId;
    }

    public <T extends SecurityLinkCreate> T setPermissionGroupId(String permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
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

    @JsonIgnore
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends SecurityLinkCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

    public String getSecurityLinkGroupId() {
        return securityLinkGroupId;
    }

    public <T extends SecurityLinkCreate> T setSecurityLinkGroupId(String securityLinkGroupId) {
        this.securityLinkGroupId = securityLinkGroupId;
        return (T) this;
    }

    @JsonIgnore
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

    @AssertTrue(message = "clazz or securedId+clazz or permissionGroupId must be provided",groups = Create.class)
    private boolean isTargetProvided() {
        return clazz!=null|| NotEmpty(permissionGroupId);
    }

    @AssertTrue(message = "operationId or operationGroupId must be provided",groups = Create.class)
    private boolean isOperationOrOperationGroupProvided() {
        return NotEmpty(operationId) || NotEmpty(operationGroupId);
    }
    private static boolean NotEmpty(String s){
        return s != null && !s.isEmpty();
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.SecurityLinkCreate securityLinkCreate) {
        securityLinkCreate.setAccess(this.access)
                .setClazz(this.clazz)
                .setOperationGroup(this.operationGroup)
                .setOperation(this.operation)
                .setSecuredId(this.securedId)
                .setSecurityLinkGroup(this.securityLinkGroup)
                .setPermissionGroup(this.permissionGroup);
        super.forService(securityLinkCreate);
    }
}
