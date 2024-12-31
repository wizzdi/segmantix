package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.model.Access;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecurityLinkFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;


    private List<SecurityLinkGroup> securityLinkGroups;

    
    private List<SecurityUser> relevantUsers;
    
    private List<Role> relevantRoles;

    
    private List<SecurityTenant> relevantTenants;



    private Set<String> securedIds =new HashSet<>();

    private List<Clazz> clazzes;
    
    private List<PermissionGroup> permissionGroups;
    
    private List<OperationGroup> operationGroups;

    
    private List<SecurityOperation> operations;
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

    
    public List<SecurityOperation> getOperations() {
        return operations;
    }

    public <T extends SecurityLinkFilter> T setOperations(List<SecurityOperation> operations) {
        this.operations = operations;
        return (T) this;
    }


    public Set<Access> getAccesses() {
        return accesses;
    }

    public <T extends SecurityLinkFilter> T setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
        return (T) this;
    }


    
    public List<SecurityUser> getRelevantUsers() {
        return relevantUsers;
    }

    public <T extends SecurityLinkFilter> T setRelevantUsers(List<SecurityUser> relevantUsers) {
        this.relevantUsers = relevantUsers;
        return (T) this;
    }

    
    public List<Role> getRelevantRoles() {
        return relevantRoles;
    }

    public <T extends SecurityLinkFilter> T setRelevantRoles(List<Role> relevantRoles) {
        this.relevantRoles = relevantRoles;
        return (T) this;
    }

    
    public List<SecurityTenant> getRelevantTenants() {
        return relevantTenants;
    }

    public <T extends SecurityLinkFilter> T setRelevantTenants(List<SecurityTenant> relevantTenants) {
        this.relevantTenants = relevantTenants;
        return (T) this;
    }


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

    
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends SecurityLinkFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }

    
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends SecurityLinkFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends SecurityLinkFilter> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }
}
