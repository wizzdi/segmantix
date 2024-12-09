package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.IRole;
import com.wizzdi.segmantix.api.ISecurityOperation;
import com.wizzdi.segmantix.api.ISecurityTenant;
import com.wizzdi.segmantix.api.ISecurityUser;

import java.util.List;

public record SecurityContext(ISecurityUser securityUser, List<ISecurityTenant> tenants, List<IRole> roles,
                              ISecurityOperation securityOperation) {
}
