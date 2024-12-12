package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.IRoleToBaseclass;
import com.wizzdi.segmantix.api.ISecurityLink;
import com.wizzdi.segmantix.api.ITenantToBaseclass;
import com.wizzdi.segmantix.api.IUserToBaseclass;
import com.wizzdi.segmantix.api.SecurityLinkService;
import com.wizzdi.segmantix.service.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityLinkServiceImpl implements SecurityLinkService {

    @Autowired
    private CacheImpl cache;
    private final List<ISecurityLink> links=new ArrayList<>();
    @Override
    public List<ISecurityLink> getSecurityLinks(SecurityContext securityContext) {
        return links.stream().filter(f-> relevantLink(f,securityContext)).toList();
    }

    private boolean relevantLink(ISecurityLink securityLink, SecurityContext securityContext) {
        Set<String> roleIds=securityContext.roles().stream().map(f->f.getId()).collect(Collectors.toSet());
        Set<String> tenantIds=securityContext.tenants().stream().map(f->f.getId()).collect(Collectors.toSet());
        return switch (securityLink){
            case IUserToBaseclass userToBaseclass->userToBaseclass.getUser().getId().equals(securityContext.securityUser().getId());
            case IRoleToBaseclass roleToBaseclass->roleIds.contains(roleToBaseclass.getRole().getId());
            case ITenantToBaseclass tenantToBaseclass->tenantIds.contains(tenantToBaseclass.getTenant().getId());
            default -> false;
        };
    }

    public void add(ISecurityLink securityLink){
       links.add(securityLink);
       cache.invalidateAll();
    }

    public void clear() {
        links.clear();
        cache.invalidateAll();
    }
}
