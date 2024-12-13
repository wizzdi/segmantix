package com.wizzdi.segmantix.jpa.store.spring.service;


import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.SecurityGroupRepository;
import com.wizzdi.segmantix.jpa.store.spring.events.BasicUpdated;
import com.wizzdi.segmantix.jpa.store.spring.request.*;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.response.SecurityGroupContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class SecurityGroupService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityGroupRepository securityGroupRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private TenantService tenantService;



	public void merge(Object o){
		securityGroupRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityGroupRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return securityGroupRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityGroupNoMerge(SecurityGroupCreate securityGroupCreate, SecurityGroup securityGroup) {
        return basicService.updateBasicNoMerge(securityGroupCreate, securityGroup);
	}

	public SecurityGroup updateSecurityGroup(SecurityGroupUpdate securityGroupUpdate, SecurityContext securityContext){
		SecurityGroup securityGroup=securityGroupUpdate.getSecurityGroup();
		if(updateSecurityGroupNoMerge(securityGroupUpdate,securityGroup)){
			securityGroupRepository.merge(securityGroup);
		}
		return securityGroup;
	}


	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return securityGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityGroup> getAllSecurityGroups(SecurityGroupFilter securityGroupFilter, SecurityContext securityContext) {
		List<SecurityGroup> list= listAllSecurityGroups(securityGroupFilter, securityContext);
		long count=securityGroupRepository.countAllSecurityGroups(securityGroupFilter,securityContext);
		return new PaginationResponse<>(list,securityGroupFilter,count);
	}

	public List<SecurityGroup> listAllSecurityGroups(SecurityGroupFilter securityGroupFilter, SecurityContext securityContext) {
		return securityGroupRepository.listAllSecurityGroups(securityGroupFilter, securityContext);
	}

	public SecurityGroup createSecurityGroup(SecurityGroupCreate securityGroupCreate, SecurityContext securityContext){
		SecurityGroup securityGroup= createSecurityGroupNoMerge(securityGroupCreate,securityContext);
		securityGroupRepository.merge(securityGroup);
		return securityGroup;
	}

	public SecurityGroup createSecurityGroupNoMerge(SecurityGroupCreate securityGroupCreate, SecurityContext securityContext){
		SecurityGroup securityGroup=new SecurityGroup();
		securityGroup.setId(UUID.randomUUID().toString());
		updateSecurityGroupNoMerge(securityGroupCreate,securityGroup);
		BaseclassService.createSecurityObjectNoMerge(securityGroup,securityContext);
		return securityGroup;
	}

	public PaginationResponse<SecurityGroupContainer> getAllSecurityGroupContainers(SecurityGroupFilter securityGroupFilter, SecurityContext securityContext) {
		SecurityFilter securityFilter = securityGroupFilter.getSecurityFilter();
		List<SecurityOrder> sorting = null;
		if (securityFilter != null) {
			sorting=securityFilter.getSorting();
			List<User> relevantUsers = securityFilter.getRelevantUsers();
			if (relevantUsers != null && !relevantUsers.isEmpty()) {
				if (securityFilter.getRelevantRoles() == null || securityFilter.getRelevantRoles().isEmpty()) {
					List<Role> roles = roleService.listAllRoles(new RoleFilter().setUsers(relevantUsers), null);
					securityFilter.setRelevantRoles(roles);
				}
				if (securityFilter.getRelevantTenants() == null || securityFilter.getRelevantTenants().isEmpty()) {
					List<Tenant> tenants = tenantService.listAllTenants(new TenantFilter().setUsers(relevantUsers), null);
					securityFilter.setRelevantTenants(tenants);
				}


			}
			if (sorting == null || sorting.isEmpty()) {
				securityFilter.setSorting(Arrays.stream(SecurityOrder.values()).toList());
			}
		}

		PaginationResponse<SecurityGroup> paginationResponse = getAllSecurityGroups(securityGroupFilter, securityContext);
		List<SecurityGroup> linkGroups = paginationResponse.getList();
		List<Security> links = linkGroups.isEmpty() ? new ArrayList<>() : securityService.listAllSecuritys(new SecurityFilter().setSorting(sorting).setSecurityGroups(linkGroups), securityContext);
		Map<String, List<Security>> linksGrouped = links.stream().collect(Collectors.groupingBy(f -> f.getSecurityGroup().getId(), LinkedHashMap::new, Collectors.toList()));
		List<SecurityGroupContainer> containers = linkGroups.stream().map(f -> new SecurityGroupContainer(f, linksGrouped.getOrDefault(f.getId(), new ArrayList<>()))).toList();
		return new PaginationResponse<>(containers,securityGroupFilter,paginationResponse.getTotalRecords());
	}

	@EventListener
	public void onSecurityGroupUpdate(BasicUpdated<SecurityGroup> securityGroupBasicUpdated) {
		SecurityGroup securityGroup = securityGroupBasicUpdated.getBaseclass();
		if (securityGroup.isSoftDelete()) {
			List<Security> securitys = securityService.listAllSecuritys(new SecurityFilter().setSecurityGroups(Collections.singletonList(securityGroup)), null);
			List<Object> toMerge = new ArrayList<>();
			for (Security security : securitys) {
				securityService.updateSecurityNoMerge(new SecurityCreate().setSoftDelete(true), security);
			}
			securityService.massMerge(toMerge);
		}
	}
}
