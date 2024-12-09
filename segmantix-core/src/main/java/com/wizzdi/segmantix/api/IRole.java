package com.wizzdi.segmantix.api;


public interface IRole extends ISecurityEntity{
    ISecurityTenant getTenant();

}
