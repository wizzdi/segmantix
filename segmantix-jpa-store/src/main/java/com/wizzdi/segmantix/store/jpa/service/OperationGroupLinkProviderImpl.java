package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup_;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OperationGroupLinkProviderImpl implements OperationGroupLinkProvider {

    private final EntityManager em;

    public OperationGroupLinkProviderImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<IOperationGroupLink> listAllOperationGroupLinks(List<IOperation> operations) {
        List<SecurityOperation> ops = operations.stream().filter(f -> f instanceof SecurityOperation).map(f -> (SecurityOperation) f).toList();
        if(ops.isEmpty()){
            return Collections.emptyList();
        }
        Set<String> opIds=ops.stream().map(f->f.getId()).collect(Collectors.toSet());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OperationToGroup> q = cb.createQuery(OperationToGroup.class);
        Root<OperationToGroup> r = q.from(OperationToGroup.class);
        q.select(r).where(r.get(OperationToGroup_.operationId).in(opIds),cb.isFalse(r.get(OperationToGroup_.softDelete)));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
