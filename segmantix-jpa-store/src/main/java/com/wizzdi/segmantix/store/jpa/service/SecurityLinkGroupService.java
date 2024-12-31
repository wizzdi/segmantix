package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.SecurityLinkGroupRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.SecurityLink;
import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.store.jpa.request.RoleFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkGroupUpdate;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkOrder;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.response.SecurityLinkContainer;
import com.wizzdi.segmantix.store.jpa.response.SecurityLinkGroupContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class SecurityLinkGroupService implements SegmantixService {

    private final BasicService basicService;
    private final SecurityLinkGroupRepository securityLinkGroupRepository;
    private final SecurityLinkService securityLinkService;
    private final RoleService roleService;
    private final SecurityTenantService securityTenantService;
    private final SecurityOperationService securityOperationService;

    public SecurityLinkGroupService(BasicService basicService, SecurityLinkGroupRepository securityLinkGroupRepository, SecurityLinkService securityLinkService, RoleService roleService, SecurityTenantService securityTenantService, SecurityOperationService securityOperationService) {
        this.basicService = basicService;
        this.securityLinkGroupRepository = securityLinkGroupRepository;
        this.securityLinkService = securityLinkService;
        this.roleService = roleService;
        this.securityTenantService = securityTenantService;
        this.securityOperationService = securityOperationService;
    }

    public void merge(Object o) {
        securityLinkGroupRepository.merge(o);
    }

    public void massMerge(List<Object> list) {
        securityLinkGroupRepository.massMerge(list);
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return securityLinkGroupRepository.listByIds(c, ids, securityContext);
    }


    public boolean updateSecurityLinkGroupNoMerge(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityLinkGroup securityLinkGroup) {
        return basicService.updateBasicNoMerge(securityLinkGroupCreate, securityLinkGroup);
    }

    public SecurityLinkGroup updateSecurityLinkGroup(SecurityLinkGroupUpdate securityLinkGroupUpdate, SecurityContext securityContext) {
        SecurityLinkGroup securityLinkGroup = securityLinkGroupUpdate.getSecurityLinkGroup();
        if (updateSecurityLinkGroupNoMerge(securityLinkGroupUpdate, securityLinkGroup)) {
            securityLinkGroupRepository.merge(securityLinkGroup);
        }
        return securityLinkGroup;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
        return securityLinkGroupRepository.getByIdOrNull(id, c, securityContext);
    }

    public PaginationResponse<SecurityLinkGroup> getAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext) {
        List<SecurityLinkGroup> list = listAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
        long count = securityLinkGroupRepository.countAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
        return new PaginationResponse<>(list, securityLinkGroupFilter, count);
    }

    public List<SecurityLinkGroup> listAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext) {
        return securityLinkGroupRepository.listAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
    }

    public SecurityLinkGroup createSecurityLinkGroup(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityContext securityContext) {
        SecurityLinkGroup securityLinkGroup = createSecurityLinkGroupNoMerge(securityLinkGroupCreate, securityContext);
        securityLinkGroupRepository.merge(securityLinkGroup);
        return securityLinkGroup;
    }

    public SecurityLinkGroup createSecurityLinkGroupNoMerge(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityContext securityContext) {
        SecurityLinkGroup securityLinkGroup = new SecurityLinkGroup();
        securityLinkGroup.setId(UUID.randomUUID().toString());
        updateSecurityLinkGroupNoMerge(securityLinkGroupCreate, securityLinkGroup);
        BaseclassService.createSecurityObjectNoMerge(securityLinkGroup, securityContext);
        return securityLinkGroup;
    }

    public PaginationResponse<SecurityLinkGroupContainer> getAllSecurityLinkGroupContainers(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext) {
        SecurityLinkFilter securityLinkFilter = securityLinkGroupFilter.getSecurityLinkFilter();
        List<SecurityLinkOrder> sorting = null;
        if (securityLinkFilter != null) {
            sorting = securityLinkFilter.getSorting();
            List<SecurityUser> relevantUsers = securityLinkFilter.getRelevantUsers();
            if (relevantUsers != null && !relevantUsers.isEmpty()) {
                if (securityLinkFilter.getRelevantRoles() == null || securityLinkFilter.getRelevantRoles().isEmpty()) {
                    List<Role> roles = roleService.listAllRoles(new RoleFilter().setUsers(relevantUsers), null);
                    securityLinkFilter.setRelevantRoles(roles);
                }
                if (securityLinkFilter.getRelevantTenants() == null || securityLinkFilter.getRelevantTenants().isEmpty()) {
                    List<SecurityTenant> securityTenants = securityTenantService.listAllTenants(new SecurityTenantFilter().setUsers(relevantUsers), null);
                    securityLinkFilter.setRelevantTenants(securityTenants);
                }


            }
            if (sorting == null || sorting.isEmpty()) {
                securityLinkFilter.setSorting(Arrays.stream(SecurityLinkOrder.values()).toList());
            }
        }

        PaginationResponse<SecurityLinkGroup> paginationResponse = getAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
        List<SecurityLinkGroup> linkGroups = paginationResponse.getList();
        List<SecurityLink> links = linkGroups.isEmpty() ? new ArrayList<>() : securityLinkService.listAllSecurityLinks(new SecurityLinkFilter().setSorting(sorting).setSecurityLinkGroups(linkGroups), securityContext);
        Map<String, List<SecurityLink>> linksGrouped = links.stream().collect(Collectors.groupingBy(f -> f.getSecurityLinkGroup().getId(), LinkedHashMap::new, Collectors.toList()));
        List<SecurityLinkGroupContainer> containers = linkGroups.stream().map(f -> new SecurityLinkGroupContainer(f, linksGrouped.getOrDefault(f.getId(), new ArrayList<>()).stream().map(e -> toLinkContainer(e)).toList())).toList();
        return new PaginationResponse<>(containers, securityLinkGroupFilter, paginationResponse.getTotalRecords());
    }

    private SecurityLinkContainer toLinkContainer(SecurityLink securityLink) {
        return new SecurityLinkContainer(securityLink.getId(), securityLink.getSecuredId(), securityLink.getSecuredType(), securityLink.getPermissionGroup(), securityLink.getOperationGroup(), securityLink.getSecurityLinkGroup(), securityLink.getAccess(), securityLink.getOperationId() != null ? securityOperationService.getByIdOrNull(securityLink.getOperationId()) : null, securityLink.getSecurityEntity());
    }

    public void deleteGroupWithLinks(SecurityLinkGroup securityLinkGroup) {
        List<SecurityLink> securityLinks = securityLinkService.listAllSecurityLinks(new SecurityLinkFilter().setSecurityLinkGroups(Collections.singletonList(securityLinkGroup)), null);
        List<Object> toMerge = new ArrayList<>();
        toMerge.add(securityLinkGroup);
        securityLinkGroup.setSoftDelete(true);
        for (SecurityLink securityLink : securityLinks) {
            securityLinkService.updateSecurityLinkNoMerge(new SecurityLinkCreate().setSoftDelete(true), securityLink);
        }
        securityLinkService.massMerge(toMerge);
    }
}
