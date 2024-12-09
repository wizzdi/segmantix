package com.wizzdi.segmantix.api;


public interface IPermissionGroupToBaseclass {
    String getSecuredId();
    String getSecuredType();
    IPermissionGroup getPermissionGroup();
}
