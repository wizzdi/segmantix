package com.wizzdi.segmantix.api.model;


import com.wizzdi.segmantix.model.Access;

public interface ISecurityLink {

   String getId();
   String getSecuredId();
   String getSecuredType();
   IInstanceGroup getInstanceGroup();
   Access getAccess();
   IOperation getOperation();
   IOperationGroup getOperationGroup();
}
