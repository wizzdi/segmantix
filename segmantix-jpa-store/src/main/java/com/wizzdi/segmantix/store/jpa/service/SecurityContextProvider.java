package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.RoleToUser;
import com.wizzdi.segmantix.store.jpa.model.RoleToUser_;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.store.jpa.model.TenantToUser;
import com.wizzdi.segmantix.store.jpa.model.TenantToUser_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityContextProvider implements SegmantixService {
    private final EntityManager em;

    public SecurityContextProvider(EntityManager em) {
        this.em = em;
    }
    public SecurityContext getSecurityContext(SecurityUser securityUser, SecurityOperation operation){
        return getSecurityContext(securityUser)
                .setOperation(operation);
    }
    public SecurityContext impersonate(SecurityContext securityContext,List<SecurityTenant> readTenants,SecurityTenant writeTenant){
        Set<String> readTenantIds=readTenants.stream().map(f->f.getTenant()).map(f->f.getId()).collect(Collectors.toSet());
        List<Role> allRoles=securityContext.getAllRoles().stream().filter(f->readTenantIds.contains(f.getTenant().getId())).toList();
        List<SecurityTenant> tenants = securityContext.getTenants().stream().filter(f->readTenantIds.contains(f.getId())||f.getId().equalsIgnoreCase(writeTenant.getId())).toList();
        SecurityTenant tenantToCreateIn = tenants.stream().filter(f->f.getId().equals(writeTenant.getId())).findFirst().orElse(null);
        Map<String, List<Role>> roleMap = allRoles.stream().collect(Collectors.groupingBy(f -> f.getTenant().getId()));
        return new SecurityContext()
                .setTenants(tenants)
                .setTenantToCreateIn(tenantToCreateIn)
                .setRoleMap(roleMap)
                .setAllRoles(allRoles)
                .setUser(securityContext.getUser());
    }

    public SecurityContext getSecurityContext(SecurityUser securityUser) {
        List<TenantToUser> links=getTenantToUserLinks(securityUser);
        List<RoleToUser> roleToUsers=getRoleToUserLinks(securityUser);
        List<Role> allRoles=new ArrayList<>(roleToUsers.stream().map(f->f.getRole()).collect(Collectors.toMap(f->f.getId(), f->f,(a, b)->a)).values());
        List<SecurityTenant> tenants = new ArrayList<>(links.stream().map(f -> f.getTenant()).collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a)).values());
        SecurityTenant tenantToCreateIn = links.stream().filter(f -> f.isDefaultTenant()).map(f -> f.getTenant()).findFirst().orElse(null);
        Map<String, List<Role>> roleMap = roleToUsers.stream().map(f -> f.getRole()).collect(Collectors.groupingBy(f -> f.getTenant().getId()));
        return new SecurityContext()
                .setTenants(tenants)
                .setTenantToCreateIn(tenantToCreateIn)
                .setRoleMap(roleMap)
                .setAllRoles(allRoles)
                .setUser(securityUser);
    }
    private List<TenantToUser> getTenantToUserLinks(SecurityUser securityUser){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TenantToUser> q=cb.createQuery(TenantToUser.class);
        Root<TenantToUser> r=q.from(TenantToUser.class);
        q.select(r).where(
                cb.equal(r.get(TenantToUser_.user),securityUser),
                cb.isFalse(r.get(TenantToUser_.softDelete))
        );
        return em.createQuery(q).getResultList();
    }

    private List<RoleToUser> getRoleToUserLinks(SecurityUser securityUser){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RoleToUser> q=cb.createQuery(RoleToUser.class);
        Root<RoleToUser> r=q.from(RoleToUser.class);
        q.select(r).where(
                cb.equal(r.get(RoleToUser_.user),securityUser),
                cb.isFalse(r.get(TenantToUser_.softDelete))
        );
        return em.createQuery(q).getResultList();
    }
}
