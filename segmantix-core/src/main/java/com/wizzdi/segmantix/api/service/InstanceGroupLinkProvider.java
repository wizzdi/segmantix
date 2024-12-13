package com.wizzdi.segmantix.api.service;



import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IInstanceGroupLink;

import java.util.List;

public interface InstanceGroupLinkProvider {
    List<IInstanceGroupLink> getInstanceGroupLinks(List<IInstanceGroup> instanceGroups);
}
