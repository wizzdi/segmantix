package com.wizzdi.segmantix.spring.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.spring.validation.ClazzValid;
import com.wizzdi.segmantix.spring.validation.IdValid;
import com.wizzdi.segmantix.spring.validation.OperationValid;
import com.wizzdi.segmantix.model.Access;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@IdValid.List({
        @IdValid(field = "securityLinkGroupIds", targetField = "securityLinkGroups", fieldType = SecurityLinkGroup.class),
        @IdValid(field = "relevantUserIds", targetField = "relevantUsers", fieldType = SecurityUser.class),

})
@OperationValid(sourceField = "operationIds", targetField = "operations")
public class SecurityLinkFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> relevantUserIds = new HashSet<>();
    private Set<String> securityLinkGroupIds = new HashSet<>();
    @JsonIgnore
    private List<SecurityLinkGroup> securityLinkGroups;

    @JsonIgnore
    private List<SecurityUser> relevantUsers;
    @JsonIgnore
    private List<Role> relevantRoles;

    @JsonIgnore
    private List<SecurityTenant> relevantTenants;


    @JsonAlias("baseclassIds")
    private Set<String> securedIds = new HashSet<>();
    @ClazzValid
    @JsonIgnore
    private List<Clazz> clazzes;
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;
    @JsonIgnore
    private List<OperationGroup> operationGroups;

    @JsonIgnore
    private List<SecurityOperation> operations;
    private Set<String> operationIds = new HashSet<>();
    private Set<Access> accesses;

    private List<SecurityLinkOrder> sorting;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityLinkFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }


    public Set<String> getSecuredIds() {
        return securedIds;
    }

    public <T extends SecurityLinkFilter> T setSecuredIds(Set<String> securedIds) {
        this.securedIds = securedIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityOperation> getOperations() {
        return operations;
    }

    public <T extends SecurityLinkFilter> T setOperations(List<SecurityOperation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public <T extends SecurityLinkFilter> T setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
        return (T) this;
    }

    public Set<Access> getAccesses() {
        return accesses;
    }

    public <T extends SecurityLinkFilter> T setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
        return (T) this;
    }

    public Set<String> getRelevantUserIds() {
        return relevantUserIds;
    }

    public <T extends SecurityLinkFilter> T setRelevantUserIds(Set<String> relevantUserIds) {
        this.relevantUserIds = relevantUserIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityUser> getRelevantUsers() {
        return relevantUsers;
    }

    public <T extends SecurityLinkFilter> T setRelevantUsers(List<SecurityUser> relevantUsers) {
        this.relevantUsers = relevantUsers;
        return (T) this;
    }

    @JsonIgnore
    public List<Role> getRelevantRoles() {
        return relevantRoles;
    }

    public <T extends SecurityLinkFilter> T setRelevantRoles(List<Role> relevantRoles) {
        this.relevantRoles = relevantRoles;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getRelevantTenants() {
        return relevantTenants;
    }

    public <T extends SecurityLinkFilter> T setRelevantTenants(List<SecurityTenant> relevantTenants) {
        this.relevantTenants = relevantTenants;
        return (T) this;
    }

    public Set<String> getSecurityLinkGroupIds() {
        return securityLinkGroupIds;
    }

    public <T extends SecurityLinkFilter> T setSecurityLinkGroupIds(Set<String> securityLinkGroupIds) {
        this.securityLinkGroupIds = securityLinkGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityLinkGroup> getSecurityLinkGroups() {
        return securityLinkGroups;
    }

    public <T extends SecurityLinkFilter> T setSecurityLinkGroups(List<SecurityLinkGroup> securityLinkGroups) {
        this.securityLinkGroups = securityLinkGroups;
        return (T) this;
    }

    public List<SecurityLinkOrder> getSorting() {
        return sorting;
    }

    public <T extends SecurityLinkFilter> T setSorting(List<SecurityLinkOrder> sorting) {
        this.sorting = sorting;
        return (T) this;
    }

    @JsonIgnore
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends SecurityLinkFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }

    @JsonIgnore
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends SecurityLinkFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    @JsonIgnore
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends SecurityLinkFilter> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter securityLinkFilter) {
        securityLinkFilter.setAccesses(accesses)
                .setClazzes(clazzes)
                .setOperations(operations)
                .setBasicPropertiesFilter(Optional.ofNullable(basicPropertiesFilter).map(f -> f.forService()).orElse(null))
                .setPermissionGroups(permissionGroups)
                .setSecuredIds(securedIds)
                .setRelevantRoles(relevantRoles)
                .setRelevantTenants(relevantTenants)
                .setRelevantUsers(relevantUsers)
                .setSorting(Optional.ofNullable(sorting).stream().flatMap(List::stream).map(f -> f.forService()).collect(Collectors.toList()))
                .setSecurityLinkGroups(securityLinkGroups)
                .setOperationGroups(operationGroups);
        super.forService(securityLinkFilter);
    }

    public com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter forService() {
        com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter securityLinkFilter=new com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter();
        forService(securityLinkFilter);
        return securityLinkFilter;
    }
}
