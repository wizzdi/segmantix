package com.wizzdi.segmantix.api;

import com.wizzdi.segmantix.service.SecurityContext;

import java.util.List;

public interface SecurityLinkService {
    List<ISecurityLink> getSecurityLinks(SecurityContext securityContext);
}
