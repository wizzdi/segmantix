package com.wizzdi.segmantix.api;



import java.util.List;

public interface PermissionGroupToBaseclassService {
    List<IPermissionGroupToBaseclass> getPermissionGroupLinks(List<IPermissionGroup> permissionGroups);
}
