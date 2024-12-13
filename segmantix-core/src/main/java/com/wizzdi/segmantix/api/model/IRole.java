package com.wizzdi.segmantix.api.model;


public interface IRole extends ISecurityEntity {

    ITenant getTenant();
    default boolean isSuperAdmin(){return false;}

}
