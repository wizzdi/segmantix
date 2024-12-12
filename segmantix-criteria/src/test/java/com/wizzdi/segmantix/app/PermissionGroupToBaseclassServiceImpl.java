package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.IPermissionGroup;
import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import com.wizzdi.segmantix.api.PermissionGroupToBaseclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionGroupToBaseclassServiceImpl implements PermissionGroupToBaseclassService {

    @Autowired
    private CacheImpl cache;
    private final List<IPermissionGroupToBaseclass> links=new ArrayList<>();
    @Override
    public List<IPermissionGroupToBaseclass> getPermissionGroupLinks(List<IPermissionGroup> permissionGroups) {
        Set<String> ids=permissionGroups.stream().map(f->f.getId()).collect(Collectors.toSet());
        return links.stream().filter(f->ids.contains(f.getPermissionGroup().getId())).toList();
    }
    public void add(IPermissionGroupToBaseclass link){
        links.add(link);
        cache.invalidateAll();

    }

    public void clear() {
        links.clear();
        cache.invalidateAll();
    }
}
