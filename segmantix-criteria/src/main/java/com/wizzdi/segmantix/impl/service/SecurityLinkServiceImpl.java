package com.wizzdi.segmantix.impl.service;

import com.wizzdi.segmantix.api.IRole;
import com.wizzdi.segmantix.api.ISecurityLink;
import com.wizzdi.segmantix.api.ISecurityTenant;
import com.wizzdi.segmantix.api.ISecurityUser;
import com.wizzdi.segmantix.api.SecurityLinkService;
import com.wizzdi.segmantix.impl.model.RoleToBaseclass;
import com.wizzdi.segmantix.impl.model.RoleToBaseclass_;
import com.wizzdi.segmantix.impl.model.SecurityLink;
import com.wizzdi.segmantix.impl.model.TenantToBaseclass;
import com.wizzdi.segmantix.impl.model.TenantToBaseclass_;
import com.wizzdi.segmantix.impl.model.UserToBaseclass;
import com.wizzdi.segmantix.impl.model.UserToBaseclass_;
import com.wizzdi.segmantix.service.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class SecurityLinkServiceImpl implements SecurityLinkService {

    private final EntityManager em;

    public SecurityLinkServiceImpl(EntityManager em) {
        this.em=em;
    }

    @Override
    public List<ISecurityLink> getSecurityLinks(SecurityContext securityContext) {
        ISecurityUser securityUser=securityContext.securityUser();
        List<IRole> roles=securityContext.roles();
        List<ISecurityTenant> tenants=securityContext.tenants();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SecurityLink> q = cb.createQuery(SecurityLink.class);
        Root<SecurityLink> r = q.from(SecurityLink.class);
        Root<UserToBaseclass> user = cb.treat(r, UserToBaseclass.class);
        Root<RoleToBaseclass> role = cb.treat(r, RoleToBaseclass.class);
        Root<TenantToBaseclass> tenant = cb.treat(r, TenantToBaseclass.class);
        q.select(r).where(
                cb.or(
                        user.get(UserToBaseclass_.user).in(securityUser),
                        roles.isEmpty()?cb.or():role.get(RoleToBaseclass_.role).in(roles),
                        tenants.isEmpty()?cb.or():tenant.get(TenantToBaseclass_.tenant).in(tenants)
                ));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
