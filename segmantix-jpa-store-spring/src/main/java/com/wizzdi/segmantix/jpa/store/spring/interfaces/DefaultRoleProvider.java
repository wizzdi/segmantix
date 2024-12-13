package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleCreate;

public interface DefaultRoleProvider<T extends Role> {

    T createRole(RoleCreate tenantToUserCreate);

}
