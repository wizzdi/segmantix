package com.wizzdi.segmantix.app;

import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.service.CriteriaApiSecurityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestEntityRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CriteriaApiSecurityRepository securedBasicRepository;


    public List<TestEntity> listAllTestEntities(
            TestEntityFilter filtering, ISecurityContext securityContext) {
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

    public Connection getConnection() throws SQLException {
      EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
    Connection connection = info.getDataSource().getConnection();
    return connection;
    }
}
