package com.wizzdi.segmantix.api.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

public interface FieldPathProvider {
    <T> Path<String> getCreatorIdPath(From<?,T> r);
    <T> Path<String> getTenantIdPath(From<?,T> r);
    <T> Path<String> getTypePath(From<?,T> r);
    <T> Path<String> getSecurityId(From<?,T> r);
    <T> Path<String> getInstanceGroupPath(From<?,T> r, CriteriaBuilder cb);
}
