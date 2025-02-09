package com.wizzdi.segmantix.api.service;


public interface JDBCFieldPathProvider {
    String getCreatorIdPath(String table);
     String getTenantIdPath(String table);
     String getTypePath(String table);
     String getSecurityId(String table);
     JDBCFieldPath getInstanceGroupPath(String table,String alias);
}
