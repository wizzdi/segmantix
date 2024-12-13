package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.impl.model.TenantToUser;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserCreate;

public interface DefaultTenantToUserProvider<T extends TenantToUser> {

    T createTenantToUser(TenantToUserCreate tenantToUserCreate);

}
