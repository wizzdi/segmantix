package com.wizzdi.segmantix.api;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

public interface FieldPathProvider {
    <T extends Secured> Path<String> getCreatorIdPath(From<?,T> r);
    <T extends Secured> Path<String> getTenantIdPath(From<?,T> r);
    <T extends Secured> Path<String> getTypePath(From<?,T> r);
    <T extends Secured> Path<String> getIdPath(From<?,T> r);
}
