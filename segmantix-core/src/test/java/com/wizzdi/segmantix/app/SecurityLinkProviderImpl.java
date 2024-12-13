package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.model.IRoleSecurity;
import com.wizzdi.segmantix.api.model.ISecurity;
import com.wizzdi.segmantix.api.model.ITenantSecurity;
import com.wizzdi.segmantix.api.model.IUserSecurity;
import com.wizzdi.segmantix.api.service.SecurityProvider;
import com.wizzdi.segmantix.model.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityProviderImpl implements SecurityProvider {

    @Autowired
    private CacheImpl cache;
    private final List<ISecurity> links=new ArrayList<>();
    @Override
    public List<ISecurity> getSecuritys(SecurityContext securityContext) {
        return links.stream().filter(f-> relevantLink(f,securityContext)).toList();
    }

    private boolean relevantLink(ISecurity security, SecurityContext securityContext) {
        Set<String> roleIds=securityContext.roles().stream().map(f->f.getId()).collect(Collectors.toSet());
        Set<String> tenantIds=securityContext.tenants().stream().map(f->f.getId()).collect(Collectors.toSet());
        return switch (security){
            case IUserSecurity userSecurity->userSecurity.getUser().getId().equals(securityContext.user().getId());
            case IRoleSecurity roleSecurity->roleIds.contains(roleSecurity.getRole().getId());
            case ITenantSecurity tenantSecurity->tenantIds.contains(tenantSecurity.getTenant().getId());
            default -> false;
        };
    }

    public void add(ISecurity security){
       links.add(security);
       cache.invalidateAll();
    }

    public void clear() {
        links.clear();
        cache.invalidateAll();
    }
}
