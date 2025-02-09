package com.wizzdi.segmantix.spring;

import com.wizzdi.segmantix.service.CriteriaApiSecurityRepository;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class TestEntityRepository {

    private final EntityManager em;
    private final CriteriaApiSecurityRepository securedBasicRepository;

    public TestEntityRepository(EntityManager em, CriteriaApiSecurityRepository securedBasicRepository) {
        this.em = em;
        this.securedBasicRepository = securedBasicRepository;
    }

    public List<TestEntity> listAllTestEntities(
            TestEntityFilter filtering, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TestEntity> q = cb.createQuery(TestEntity.class);
        Root<TestEntity> r = q.from(TestEntity.class);
        List<Predicate> preds = new ArrayList<>();
        securedBasicRepository.addSecurityPredicates(em, cb, q, r, preds, securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<TestEntity> query = em.createQuery(q);
        return query.getResultList();
    }


    @Transactional
    public void merge(Object base) {
        em.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        for (Object o : toMerge) {

            em.merge(o);
        }
    }
}
