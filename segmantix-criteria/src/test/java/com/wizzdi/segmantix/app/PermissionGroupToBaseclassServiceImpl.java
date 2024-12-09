package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.IPermissionGroup;
import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import com.wizzdi.segmantix.api.PermissionGroupToBaseclassService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionGroupToBaseclassServiceImpl implements PermissionGroupToBaseclassService {
    @Override
    public List<IPermissionGroupToBaseclass> getPermissionGroupLinks(List<IPermissionGroup> permissionGroups) {
        return List.of();
    }
}
