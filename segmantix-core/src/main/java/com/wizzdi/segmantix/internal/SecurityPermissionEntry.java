package com.wizzdi.segmantix.internal;

import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record SecurityPermissionEntry<T extends ISecurityEntity>(T entity,
                                                                List<SecuredHolder> allowed, List<SecuredHolder> denied,
                                                                List<SecuredTypeHolder> allowedTypes, List<SecuredTypeHolder> deniedTypes,
                                                                 List<IInstanceGroup> allowedPermissionGroups , List<IInstanceGroup> deniedPermissionGroups,
                                                               boolean allowAll) {

    public SecurityPermissionEntry {
    }

    public SecurityPermissionEntry(T entity, List<SecuredHolder> allowed, List<SecuredHolder> denied, List<SecuredTypeHolder> allowedTypes, List<SecuredTypeHolder> deniedTypes,List<IInstanceGroup> allowedPermissionGroups, List<IInstanceGroup> deniedPermissionGroups,String allTypesId) {
        this(entity, allowed, denied, allowedTypes, deniedTypes,allowedPermissionGroups, deniedPermissionGroups, allowedTypes.stream().anyMatch(f-> allTypesId.equals(f.type())));
    }

    public static <T extends ISecurityEntity,E extends ISecurityLink> SecurityPermissionEntry<T> of(T t, List<E> links, String allTypesId, Set<String> types) {
        List<SecuredHolder> allowed = links.stream().filter(f -> f.getAccess().equals(Access.allow) && f.getSecuredId() != null && f.getSecuredType() != null).map(f -> new SecuredHolder(f.getSecuredType(), f.getSecuredId())).collect(Collectors.toList());
        List<SecuredHolder> denied = links.stream().filter(f -> f.getAccess().equals(Access.deny) && f.getSecuredId() != null && f.getSecuredType() != null).map(f -> new SecuredHolder(f.getSecuredType(), f.getSecuredId())).collect(Collectors.toList());
        allowed=allowed.stream().filter(f->types.contains(f.type())).toList();
        denied=denied.stream().filter(f->types.contains(f.type())).toList();
        List<SecuredTypeHolder> allowedTypes = links.stream().filter(f -> f.getAccess().equals(Access.allow) && f.getSecuredType() != null && f.getSecuredId() == null&&types.contains(f.getSecuredType())).map(f -> new SecuredTypeHolder(f.getSecuredType())).toList();
        List<SecuredTypeHolder> deniedTypes = links.stream().filter(f -> f.getAccess().equals(Access.deny) && f.getSecuredType() != null && f.getSecuredId() == null&&types.contains(f.getSecuredType())).map(f -> new SecuredTypeHolder(f.getSecuredType())).toList();
        return new SecurityPermissionEntry<>(t,
                allowed,
                denied,
                allowedTypes,
                deniedTypes,
                links.stream().filter(f -> f.getAccess().equals(Access.allow)&&f.getInstanceGroup()!=null).map(f -> f.getInstanceGroup()).toList(),
                links.stream().filter(f -> f.getAccess().equals(Access.deny)&&f.getInstanceGroup()!=null).map(f -> f.getInstanceGroup()).toList(),
                allTypesId
                );

    }


}
