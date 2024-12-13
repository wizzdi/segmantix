package com.wizzdi.segmantix.impl.service;

import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ISecurity;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.service.SecurityProvider;
import com.wizzdi.segmantix.impl.model.RoleSecurity;
import com.wizzdi.segmantix.impl.model.Security;
import com.wizzdi.segmantix.impl.model.TenantSecurity;
import com.wizzdi.segmantix.impl.model.UserSecurity;
import com.wizzdi.segmantix.model.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class SecurityProviderImpl implements SecurityProvider {

    private final EntityManager em;

    public SecurityProviderImpl(EntityManager em) {
        this.em=em;
    }

    @Override
    public List<ISecurity> getSecuritys(SecurityContext securityContext) {
        IUser user=securityContext.user();
        List<IRole> roles=securityContext.roles();
        List<ITenant> tenants=securityContext.tenants();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Security> q = cb.createQuery(Security.class);
        Root<Security> r = q.from(Security.class);
        Root<UserSecurity> userR = cb.treat(r, UserSecurity.class);
        Root<RoleSecurity> role = cb.treat(r, RoleSecurity.class);
        Root<TenantSecurity> tenant = cb.treat(r, TenantSecurity.class);
        q.select(r).where(
                cb.or(
                        userR.get(UserSecurity_.user).in(user),
                        roles.isEmpty()?cb.or():role.get(RoleSecurity_.role).in(roles),
                        tenants.isEmpty()?cb.or():tenant.get(TenantSecurity_.tenant).in(tenants)
                ));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
