package com.wizzdi.segmantix.api.service;

import com.wizzdi.segmantix.api.model.ISecurity;
import com.wizzdi.segmantix.api.model.ISecurityContext;

import java.util.List;

public interface SecurityLinkProvider {
    List<ISecurity> getSecurityLinks(ISecurityContext securityContext);
}
