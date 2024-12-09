package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.IRole;
import com.wizzdi.segmantix.api.ISecurityTenant;
import com.wizzdi.segmantix.api.ISecurityUser;


import java.util.List;

public record SecurityPermissions(SecurityPermissionEntry<ISecurityUser> userPermissions, List<SecurityPermissionEntry<IRole>> rolePermissions, List<SecurityPermissionEntry<ISecurityTenant>> tenantPermissions) {

}
