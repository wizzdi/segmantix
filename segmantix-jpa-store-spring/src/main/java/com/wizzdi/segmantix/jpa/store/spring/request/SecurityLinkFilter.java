package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.OperationGroup;
import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.SecurityGroup;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(field = "securityGroupIds", targetField = "securityGroups", fieldType = SecurityGroup.class),
        @IdValid(field = "baseclassIds", targetField = "baseclasses", fieldType = Baseclass.class),
        @IdValid(field = "operationIds", targetField = "operations", fieldType = Operation.class),
        @IdValid(field = "relevantUserIds", targetField = "relevantUsers", fieldType = User.class),

})
public class SecurityFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> relevantUserIds = new HashSet<>();
    private Set<String> securityGroupIds = new HashSet<>();
    @JsonIgnore
    private List<SecurityGroup> securityGroups;

    @JsonIgnore
    private List<User> relevantUsers;
    @JsonIgnore
    private List<Role> relevantRoles;

    @JsonIgnore
    private List<Tenant> relevantTenants;

    @JsonIgnore
    private List<Baseclass> baseclasses;

    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<Clazz> clazzes;
    @JsonIgnore
    private List<InstanceGroup> instanceGroups;
    @JsonIgnore
    private List<OperationGroup> operationGroups;

    @JsonIgnore
    private List<Operation> operations;
    private Set<String> operationIds=new HashSet<>();
    private Set<IOperation.Access> accesses;

    private List<SecurityOrder> sorting;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends SecurityFilter> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends SecurityFilter> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Operation> getOperations() {
        return operations;
    }

    public <T extends SecurityFilter> T setOperations(List<Operation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public <T extends SecurityFilter> T setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
        return (T) this;
    }

    public Set<IOperation.Access> getAccesses() {
        return accesses;
    }

    public <T extends SecurityFilter> T setAccesses(Set<IOperation.Access> accesses) {
        this.accesses = accesses;
        return (T) this;
    }

    public Set<String> getRelevantUserIds() {
        return relevantUserIds;
    }

    public <T extends SecurityFilter> T setRelevantUserIds(Set<String> relevantUserIds) {
        this.relevantUserIds = relevantUserIds;
        return (T) this;
    }

    @JsonIgnore
    public List<User> getRelevantUsers() {
        return relevantUsers;
    }

    public <T extends SecurityFilter> T setRelevantUsers(List<User> relevantUsers) {
        this.relevantUsers = relevantUsers;
        return (T) this;
    }

    @JsonIgnore
    public List<Role> getRelevantRoles() {
        return relevantRoles;
    }

    public <T extends SecurityFilter> T setRelevantRoles(List<Role> relevantRoles) {
        this.relevantRoles = relevantRoles;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getRelevantTenants() {
        return relevantTenants;
    }

    public <T extends SecurityFilter> T setRelevantTenants(List<Tenant> relevantTenants) {
        this.relevantTenants = relevantTenants;
        return (T) this;
    }

    public Set<String> getSecurityGroupIds() {
        return securityGroupIds;
    }

    public <T extends SecurityFilter> T setSecurityGroupIds(Set<String> securityGroupIds) {
        this.securityGroupIds = securityGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    public <T extends SecurityFilter> T setSecurityGroups(List<SecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
        return (T) this;
    }

    public List<SecurityOrder> getSorting() {
        return sorting;
    }

    public <T extends SecurityFilter> T setSorting(List<SecurityOrder> sorting) {
        this.sorting = sorting;
        return (T) this;
    }

    @JsonIgnore
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends SecurityFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }

    @JsonIgnore
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends SecurityFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    @JsonIgnore
    public List<InstanceGroup> getInstanceGroups() {
        return instanceGroups;
    }

    public <T extends SecurityFilter> T setInstanceGroups(List<InstanceGroup> instanceGroups) {
        this.instanceGroups = instanceGroups;
        return (T) this;
    }
}
