package com.wizzdi.segmantix.api;


public interface ISecurityLink {

   String getId();
   String getSecuredId();
   String getSecuredType();
   IPermissionGroup getPermissionGroup();
   Access getAccess();
   ISecurityOperation getOperation();
   IOperationGroup getOperationGroup();
}
