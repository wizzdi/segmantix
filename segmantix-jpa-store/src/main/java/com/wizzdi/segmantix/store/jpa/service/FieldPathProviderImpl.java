package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass_;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;

public class FieldPathProviderImpl implements FieldPathProvider {
    @Override
    public <T> Path<String> getCreatorIdPath(From<?, T> r) {
        if (!Baseclass.class.isAssignableFrom(r.getJavaType())) {
            return null;
        }
        return r.get("creator").get("id");
    }

    @Override
    public <T> Path<String> getTenantIdPath(From<?, T> r) {
        if (!Baseclass.class.isAssignableFrom(r.getJavaType())) {
            return null;
        }
        return r.get("tenant").get("id");
    }

    @Override
    public <T> Path<String> getTypePath(From<?, T> r) {
        if (!Baseclass.class.isAssignableFrom(r.getJavaType())) {
            return null;
        }
        try {
            return r.get("dtype");
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    public <T> Path<String> getSecurityId(From<?, T> r) {
        if (!Baseclass.class.isAssignableFrom(r.getJavaType())) {
            return null;
        }
        return r.get("securityId");
    }

    @Override
    public <T> Path<String> getInstanceGroupPath(From<?, T> r, CriteriaBuilder cb) {

        Join<T, PermissionGroupToBaseclass> links = r.join(PermissionGroupToBaseclass_.relatedPermissionGroups.getName(), JoinType.LEFT);
        return links.get(PermissionGroupToBaseclass_.permissionGroup).get(PermissionGroup_.id);
    }
}
