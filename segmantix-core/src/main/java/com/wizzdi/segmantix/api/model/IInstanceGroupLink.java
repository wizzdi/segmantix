package com.wizzdi.segmantix.api.model;


public interface IInstanceGroupLink {
    String getSecuredId();
    String getSecuredType();
    IInstanceGroup getInstanceGroup();
}
