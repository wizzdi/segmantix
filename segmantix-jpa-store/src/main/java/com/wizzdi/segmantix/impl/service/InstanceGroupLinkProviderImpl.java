package com.wizzdi.segmantix.impl.service;

import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IInstanceGroupLink;
import com.wizzdi.segmantix.api.service.InstanceGroupLinkProvider;
import com.wizzdi.segmantix.impl.model.InstanceGroupLink;
import com.wizzdi.segmantix.impl.model.InstanceGroupLink_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class InstanceGroupLinkProviderImpl implements InstanceGroupLinkProvider {

    private final EntityManager em;

    public InstanceGroupLinkProviderImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<IInstanceGroupLink> getInstanceGroupLinks(List<IInstanceGroup> instanceGroups) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InstanceGroupLink> q = cb.createQuery(InstanceGroupLink.class);
        Root<InstanceGroupLink> r = q.from(InstanceGroupLink.class);
        q.select(r).where(r.get(InstanceGroupLink_.instanceGroup).in(instanceGroups));
        return new ArrayList<>(em.createQuery(q).getResultList());

    }
}
