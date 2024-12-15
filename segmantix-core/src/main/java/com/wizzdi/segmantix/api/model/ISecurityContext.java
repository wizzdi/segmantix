package com.wizzdi.segmantix.api.model;

import java.util.List;

public interface ISecurityContext {
    IUser user();
    List<? extends ITenant> tenants();
    List<? extends IRole> roles();
    IOperation operation();
}
