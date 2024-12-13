package com.wizzdi.segmantix.api.service;

import com.wizzdi.segmantix.api.model.ISecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import java.util.List;

public interface SecurityProvider {
    List<ISecurity> getSecuritys(SecurityContext securityContext);
}
