package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass_;
import com.wizzdi.segmantix.store.jpa.model.SecurityLink;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass_;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SecurityProviderImpl implements SecurityLinkProvider {
    private static final Logger logger= LoggerFactory.getLogger(SecurityProviderImpl.class);

    private final EntityManager em;

    public SecurityProviderImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ISecurityLink> getSecurityLinks(ISecurityContext securityContext) {
        logger.info("fetching links");
        IUser user=securityContext.user();
        List<? extends IRole> roles=securityContext.roles();
        List<? extends ITenant> tenants=securityContext.tenants();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SecurityLink> q = cb.createQuery(SecurityLink.class);
        Root<SecurityLink> r = q.from(SecurityLink.class);
        Root<UserToBaseclass> userR = cb.treat(r, UserToBaseclass.class);
        Root<RoleToBaseclass> role = cb.treat(r, RoleToBaseclass.class);
        Root<TenantToBaseclass> tenant = cb.treat(r, TenantToBaseclass.class);
        q.select(r).where(
                cb.or(
                        userR.get(UserToBaseclass_.user).in(user),
                        roles.isEmpty()?cb.or():role.get(RoleToBaseclass_.role).in(roles),
                        tenants.isEmpty()?cb.or():tenant.get(TenantToBaseclass_.tenant).in(tenants)
                ));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
