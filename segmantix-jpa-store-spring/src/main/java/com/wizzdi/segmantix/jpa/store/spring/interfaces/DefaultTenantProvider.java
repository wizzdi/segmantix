package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantCreate;

public interface DefaultTenantProvider<T extends Tenant> {

    T createDefaultTenant(TenantCreate tenantCreate);

}
