package com.wizzdi.segmantix.model;

import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;

import java.util.List;

public record SecurityContext(IUser user, List<ITenant> tenants,ITenant tenantToCreateIn, List<IRole> roles,
                              IOperation operation) {
}
