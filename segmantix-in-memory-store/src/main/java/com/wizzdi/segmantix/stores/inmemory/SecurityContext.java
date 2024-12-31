package com.wizzdi.segmantix.stores.inmemory;

import com.wizzdi.segmantix.api.model.ISecurityContext;

import java.util.List;

public record SecurityContext(User user, List<Tenant> tenants, Tenant tenantToCreateIn, List<Role> roles,
                       Operation operation) implements ISecurityContext {

}
