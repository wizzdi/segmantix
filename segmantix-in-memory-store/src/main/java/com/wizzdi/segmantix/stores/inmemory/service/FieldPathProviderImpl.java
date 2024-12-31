package com.wizzdi.segmantix.stores.inmemory.service;

import com.wizzdi.segmantix.api.service.FieldPathProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

public class FieldPathProviderImpl implements FieldPathProvider {

    private Path<String> getTypeFieldOrNull(From<?, ?> r, String name) {
        try {
            return r.get(name);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public <T> Path<String> getCreatorIdPath(From<?, T> r) {
        return r.get("creatorId");
    }

    @Override
    public <T> Path<String> getTenantIdPath(From<?, T> r) {
        return r.get("tenantId");
    }

    @Override
    public <T> Path<String> getTypePath(From<?, T> r) {
        return getTypeFieldOrNull(r, "type");
    }

    @Override
    public <T> Path<String> getSecurityId(From<?, T> r) {
        return r.get("id");
    }

    @Override
    public <T> Path<String> getInstanceGroupPath(From<?, T> r, CriteriaBuilder cb) {
        return r.get("permissionGroupId");
    }

}
