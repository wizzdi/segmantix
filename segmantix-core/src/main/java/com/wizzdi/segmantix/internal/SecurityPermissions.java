package com.wizzdi.segmantix.internal;

import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;


import java.util.List;

public record SecurityPermissions(SecurityPermissionEntry<IUser> userPermissions, List<SecurityPermissionEntry<IRole>> rolePermissions, List<SecurityPermissionEntry<ITenant>> tenantPermissions) {

}
