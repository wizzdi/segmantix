package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IInstanceGroupLink;
import com.wizzdi.segmantix.api.service.InstanceGroupLinkProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InstanceGroupLinkProviderImpl implements InstanceGroupLinkProvider {

    @Autowired
    private CacheImpl cache;
    private final List<IInstanceGroupLink> links=new ArrayList<>();
    @Override
    public List<IInstanceGroupLink> getInstanceGroupLinks(List<IInstanceGroup> instanceGroups) {
        Set<String> ids=instanceGroups.stream().map(f->f.getId()).collect(Collectors.toSet());
        return links.stream().filter(f->ids.contains(f.getInstanceGroup().getId())).toList();
    }
    public void add(IInstanceGroupLink link){
        links.add(link);
        cache.invalidateAll();

    }

    public void clear() {
        links.clear();
        cache.invalidateAll();
    }
}
