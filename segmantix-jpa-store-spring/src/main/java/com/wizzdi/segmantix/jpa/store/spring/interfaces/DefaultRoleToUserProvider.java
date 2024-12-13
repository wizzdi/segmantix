package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.impl.model.RoleToUser;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserCreate;

public interface DefaultRoleToUserProvider<T extends RoleToUser> {

    T createRoleToUser(RoleToUserCreate roleToUserCreate);

}
