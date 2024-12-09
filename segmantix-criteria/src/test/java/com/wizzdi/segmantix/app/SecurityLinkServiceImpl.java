package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.ISecurityLink;
import com.wizzdi.segmantix.api.SecurityLinkService;
import com.wizzdi.segmantix.service.SecurityContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityLinkServiceImpl implements SecurityLinkService {
    @Override
    public List<ISecurityLink> getSecurityLinks(SecurityContext securityContext) {
        return List.of();
    }
}
