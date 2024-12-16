package com.wizzdi.segmantix.api.service;

import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityContext;

import java.util.List;

public interface SecurityLinkProvider {
    List<ISecurityLink> getSecurityLinks(ISecurityContext securityContext);
}
