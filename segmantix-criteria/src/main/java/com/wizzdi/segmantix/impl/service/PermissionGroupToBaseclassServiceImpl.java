package com.wizzdi.segmantix.impl.service;

import com.wizzdi.segmantix.api.IPermissionGroup;
import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import com.wizzdi.segmantix.api.PermissionGroupToBaseclassService;
import com.wizzdi.segmantix.impl.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.impl.model.PermissionGroupToBaseclass_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class PermissionGroupToBaseclassServiceImpl implements PermissionGroupToBaseclassService {

    private final EntityManager em;

    public PermissionGroupToBaseclassServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<IPermissionGroupToBaseclass> getPermissionGroupLinks(List<IPermissionGroup> permissionGroups) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PermissionGroupToBaseclass> q = cb.createQuery(PermissionGroupToBaseclass.class);
        Root<PermissionGroupToBaseclass> r = q.from(PermissionGroupToBaseclass.class);
        q.select(r).where(r.get(PermissionGroupToBaseclass_.permissionGroup).in(permissionGroups));
        return new ArrayList<>(em.createQuery(q).getResultList());

    }
}
