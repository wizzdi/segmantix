package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;

import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.OperationGroup;
import com.wizzdi.segmantix.impl.model.SecurityGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import com.wizzdi.segmantix.model.Access;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;

@IdValid.List({
        @IdValid(targetField = "securityGroup",field = "securityGroupId",fieldType = SecurityGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "baseclass",field = "baseclassId",fieldType = Baseclass.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operation",field = "operationId",fieldType = Operation.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operationGroup",field = "operationGroupId",fieldType = OperationGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "instanceGroup",field = "instanceGroupId",fieldType = InstanceGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "clazz",field = "clazzId",fieldType = Clazz.class, groups = {Create.class, Update.class})
})
public class SecurityCreate extends BasicCreate {

    private String securityGroupId;
    @JsonIgnore
    private SecurityGroup securityGroup;

    @JsonIgnore
    private Baseclass baseclass;

    private String baseclassId;

    @JsonIgnore
    private InstanceGroup instanceGroup;

    private String instanceGroupId;

    @JsonIgnore
    private Clazz clazz;

    private String clazzId;
    private String operationId;
    @JsonIgnore
    private Operation operation;

    private String operationGroupId;
    @JsonIgnore
    private OperationGroup operationGroup;

    @NotNull(groups = Create.class)
    private Access access;

    @JsonIgnore
    public Baseclass getBaseclass() {
        return baseclass;
    }

    public <T extends SecurityCreate> T setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    public String getTenantId() {
        return baseclassId;
    }

    public <T extends SecurityCreate> T setBaseclassId(String baseclassId) {
        this.baseclassId = baseclassId;
        return (T) this;
    }

    public String getOperationId() {
        return operationId;
    }

    public <T extends SecurityCreate> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public <T extends SecurityCreate> T setOperation(Operation operation) {
        this.operation = operation;
        return (T) this;
    }

    public Access getAccess() {
        return access;
    }

    public <T extends SecurityCreate> T setAccess(Access access) {
        this.access = access;
        return (T) this;
    }

    public String getBaseclassId() {
        return baseclassId;
    }

    @JsonIgnore
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    public <T extends SecurityCreate> T setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
        return (T) this;
    }

    public String getInstanceGroupId() {
        return instanceGroupId;
    }

    public <T extends SecurityCreate> T setInstanceGroupId(String instanceGroupId) {
        this.instanceGroupId = instanceGroupId;
        return (T) this;
    }

    @JsonIgnore
    public Clazz getClazz() {
        return clazz;
    }

    public <T extends SecurityCreate> T setClazz(Clazz clazz) {
        this.clazz = clazz;
        return (T) this;
    }

    public String getClazzId() {
        return clazzId;
    }

    public <T extends SecurityCreate> T setClazzId(String clazzId) {
        this.clazzId = clazzId;
        return (T) this;
    }

    public String getOperationGroupId() {
        return operationGroupId;
    }

    public <T extends SecurityCreate> T setOperationGroupId(String operationGroupId) {
        this.operationGroupId = operationGroupId;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends SecurityCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public <T extends SecurityCreate> T setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityGroup getSecurityGroup() {
        return securityGroup;
    }

    public <T extends SecurityCreate> T setSecurityGroup(SecurityGroup securityGroup) {
        this.securityGroup = securityGroup;
        return (T) this;
    }

    @AssertTrue(message = "clazzId or baseclassId or instanceGroupId must be provided",groups = Create.class)
    private boolean isTargetProvided() {
        return !StringUtils.isEmpty(clazzId)||!StringUtils.isEmpty(baseclassId)||!StringUtils.isEmpty(instanceGroupId);
    }

    @AssertTrue(message = "operationId or operationGroupId must be provided",groups = Create.class)
    private boolean isOperationOrOperationGroupProvided() {
        return !StringUtils.isEmpty(operationId)||!StringUtils.isEmpty(operationGroupId);
    }

}
