package com.wizzdi.segmantix.service;

import com.wizzdi.segmantix.api.Access;
import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import com.wizzdi.segmantix.api.ISecurityEntity;
import com.wizzdi.segmantix.api.ISecurityLink;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SecurityPermissionEntry<T extends ISecurityEntity>(T entity,
                                                                List<SecuredHolder> allowed, List<SecuredHolder> denied,
                                                                List<SecuredTypeHolder> allowedTypes, List<SecuredTypeHolder> deniedTypes,
                                                               boolean allowAll) {

    public static final String SECURITY_WILDCARD_PLACEHOLDER="***WILDCARD***";
    public SecurityPermissionEntry {
    }

    public SecurityPermissionEntry(T entity, List<SecuredHolder> allowed, List<SecuredHolder> denied, List<SecuredTypeHolder> allowedTypes, List<SecuredTypeHolder> deniedTypes) {
        this(entity, allowed, denied, allowedTypes, deniedTypes, allowedTypes.stream().anyMatch(f-> SECURITY_WILDCARD_PLACEHOLDER.equals(f.type())));
    }

    public static <T extends ISecurityEntity,E extends ISecurityLink> SecurityPermissionEntry<T> of(T t, List<E> links, Map<String, List<IPermissionGroupToBaseclass>> permissionGroupToBaseclasses) {
        List<SecuredHolder> allowed = links.stream().filter(f -> f.getAccess().equals(Access.allow) && f.getSecuredId() != null && f.getSecuredType() != null).map(f -> new SecuredHolder(f.getSecuredType(), f.getSecuredId())).collect(Collectors.toList());
        List<SecuredHolder> denied = links.stream().filter(f -> f.getAccess().equals(Access.deny) && f.getSecuredId() != null && f.getSecuredType() != null).map(f -> new SecuredHolder(f.getSecuredType(), f.getSecuredId())).collect(Collectors.toList());
        allowed.addAll(links.stream().filter(f -> f.getAccess().equals(Access.allow)&&f.getPermissionGroup()!=null).map(f -> f.getPermissionGroup()).map(f->permissionGroupToBaseclasses.getOrDefault(f.getId(), Collections.emptyList())).flatMap(List::stream).map(f->new SecuredHolder(f.getSecuredType(),f.getSecuredId())).toList());
        denied.addAll(links.stream().filter(f -> f.getAccess().equals(Access.deny)&&f.getPermissionGroup()!=null).map(f -> f.getPermissionGroup()).map(f->permissionGroupToBaseclasses.getOrDefault(f.getId(), Collections.emptyList())).flatMap(List::stream).map(f->new SecuredHolder(f.getSecuredType(),f.getSecuredId())).toList());
        return new SecurityPermissionEntry<>(t,
                allowed,
                denied,
                links.stream().filter(f -> f.getAccess().equals(Access.allow)&&f.getSecuredType()!=null&&f.getSecuredId()==null).map(f ->new SecuredTypeHolder(f.getSecuredType())).toList(),
                links.stream().filter(f -> f.getAccess().equals(Access.deny)&&f.getSecuredType()!=null&&f.getSecuredId()==null).map(f -> new SecuredTypeHolder(f.getSecuredType())).toList()
                );

    }


}
