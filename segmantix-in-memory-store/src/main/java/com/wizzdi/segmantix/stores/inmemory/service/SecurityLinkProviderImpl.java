package com.wizzdi.segmantix.stores.inmemory.service;

import com.wizzdi.segmantix.api.model.IRoleSecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ITenantSecurityLink;
import com.wizzdi.segmantix.api.model.IUserSecurityLink;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.app.CacheImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class SecurityLinkProviderImpl implements SecurityLinkProvider {

    @Autowired
    private CacheImpl cache;
    private final List<ISecurityLink> links=new ArrayList<>();
    @Override
    public List<ISecurityLink> getSecurityLinks(ISecurityContext securityContext) {
        return links.stream().filter(f-> relevantLink(f,securityContext)).toList();
    }

    private boolean relevantLink(ISecurityLink security, ISecurityContext securityContext) {
        Set<String> roleIds=securityContext.roles().stream().map(f->f.getId()).collect(Collectors.toSet());
        Set<String> tenantIds=securityContext.tenants().stream().map(f->f.getId()).collect(Collectors.toSet());
        return switch (security){
            case IUserSecurityLink userSecurity->userSecurity.getUser().getId().equals(securityContext.user().getId());
            case IRoleSecurityLink roleSecurity->roleIds.contains(roleSecurity.getRole().getId());
            case ITenantSecurityLink tenantSecurity->tenantIds.contains(tenantSecurity.getTenant().getId());
            default -> false;
        };
    }

    public void add(ISecurityLink security){
       links.add(security);
       cache.invalidateAll();
    }

    public void clear() {
        links.clear();
        cache.invalidateAll();
    }
}
