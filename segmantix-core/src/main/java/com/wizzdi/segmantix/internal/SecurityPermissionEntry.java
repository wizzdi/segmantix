package com.wizzdi.segmantix.internal;

import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.api.model.IInstanceGroupLink;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SecurityPermissionEntry<T extends ISecurityEntity>(T entity,
                                                                List<SecuredHolder> allowed, List<SecuredHolder> denied,
                                                                List<SecuredTypeHolder> allowedTypes, List<SecuredTypeHolder> deniedTypes,
                                                               boolean allowAll) {

    public SecurityPermissionEntry {
    }

    public SecurityPermissionEntry(T entity, List<SecuredHolder> allowed, List<SecuredHolder> denied, List<SecuredTypeHolder> allowedTypes, List<SecuredTypeHolder> deniedTypes,String allTypesId) {
        this(entity, allowed, denied, allowedTypes, deniedTypes, allowedTypes.stream().anyMatch(f-> allTypesId.equals(f.type())));
    }

    public static <T extends ISecurityEntity,E extends ISecurityLink> SecurityPermissionEntry<T> of(T t, List<E> links, Map<String, List<IInstanceGroupLink>> instanceGroupLinkes, String allTypesId) {
        List<SecuredHolder> allowed = links.stream().filter(f -> f.getAccess().equals(Access.allow) && f.getSecuredId() != null && f.getSecuredType() != null).map(f -> new SecuredHolder(f.getSecuredType(), f.getSecuredId())).collect(Collectors.toList());
        List<SecuredHolder> denied = links.stream().filter(f -> f.getAccess().equals(Access.deny) && f.getSecuredId() != null && f.getSecuredType() != null).map(f -> new SecuredHolder(f.getSecuredType(), f.getSecuredId())).collect(Collectors.toList());
        allowed.addAll(links.stream().filter(f -> f.getAccess().equals(Access.allow)&&f.getInstanceGroup()!=null).map(f -> f.getInstanceGroup()).map(f->instanceGroupLinkes.getOrDefault(f.getId(), Collections.emptyList())).flatMap(List::stream).map(f->new SecuredHolder(f.getSecuredType(),f.getSecuredId())).toList());
        denied.addAll(links.stream().filter(f -> f.getAccess().equals(Access.deny)&&f.getInstanceGroup()!=null).map(f -> f.getInstanceGroup()).map(f->instanceGroupLinkes.getOrDefault(f.getId(), Collections.emptyList())).flatMap(List::stream).map(f->new SecuredHolder(f.getSecuredType(),f.getSecuredId())).toList());
        return new SecurityPermissionEntry<>(t,
                allowed,
                denied,
                links.stream().filter(f -> f.getAccess().equals(Access.allow)&&f.getSecuredType()!=null&&f.getSecuredId()==null).map(f ->new SecuredTypeHolder(f.getSecuredType())).toList(),
                links.stream().filter(f -> f.getAccess().equals(Access.deny)&&f.getSecuredType()!=null&&f.getSecuredId()==null).map(f -> new SecuredTypeHolder(f.getSecuredType())).toList(),
                allTypesId
                );

    }


}
