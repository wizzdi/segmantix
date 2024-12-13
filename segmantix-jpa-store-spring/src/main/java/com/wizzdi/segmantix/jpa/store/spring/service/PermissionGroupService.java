package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.InstanceGroup;
import com.flexicore.model.InstanceGroupLink;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.InstanceGroupRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.*;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Component
public class InstanceGroupService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private InstanceGroupRepository instanceGroupRepository;
	@Autowired
	private InstanceGroupLinkService instanceGroupLinkService;


	public InstanceGroup createInstanceGroup(InstanceGroupCreate instanceGroupCreate, SecurityContext securityContext){
		InstanceGroup instanceGroup= createInstanceGroupNoMerge(instanceGroupCreate,securityContext);
		instanceGroupRepository.merge(instanceGroup);
		return instanceGroup;
	}

	public void merge(Object o){
		instanceGroupRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		instanceGroupRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return instanceGroupRepository.listByIds(c, ids, securityContext);
	}

	public InstanceGroup createInstanceGroupNoMerge(InstanceGroupCreate instanceGroupCreate, SecurityContext securityContext){
		InstanceGroup instanceGroup=new InstanceGroup();
		instanceGroup.setId(UUID.randomUUID().toString());
		updateInstanceGroupNoMerge(instanceGroupCreate,instanceGroup);
		BaseclassService.createSecurityObjectNoMerge(instanceGroup,securityContext);
		return instanceGroup;
	}

	public boolean updateInstanceGroupNoMerge(InstanceGroupCreate instanceGroupCreate, InstanceGroup instanceGroup) {
		boolean update = basicService.updateBasicNoMerge(instanceGroupCreate, instanceGroup);
		if(instanceGroupCreate.getExternalId()!=null&&!instanceGroupCreate.getExternalId().equals(instanceGroup.getExternalId())){
			instanceGroup.setExternalId(instanceGroupCreate.getExternalId());
			update=true;
		}
		return update;
	}

	public InstanceGroup updateInstanceGroup(InstanceGroupUpdate instanceGroupUpdate, SecurityContext securityContext){
		InstanceGroup instanceGroup=instanceGroupUpdate.getInstanceGroup();
		if(updateInstanceGroupNoMerge(instanceGroupUpdate,instanceGroup)){
			instanceGroupRepository.merge(instanceGroup);
		}
		return instanceGroup;
	}

	@Deprecated
	public void validate(InstanceGroupCreate instanceGroupCreate, SecurityContext securityContext) {
		basicService.validate(instanceGroupCreate,securityContext);
	}

	@Deprecated
	public void validate(InstanceGroupFilter instanceGroupFilter, SecurityContext securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return instanceGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<InstanceGroup> getAllInstanceGroups(InstanceGroupFilter instanceGroupFilter, SecurityContext securityContext) {
		List<InstanceGroup> list= listAllInstanceGroups(instanceGroupFilter, securityContext);
		long count=instanceGroupRepository.countAllInstanceGroups(instanceGroupFilter,securityContext);
		return new PaginationResponse<>(list,instanceGroupFilter,count);
	}

	public List<InstanceGroup> listAllInstanceGroups(InstanceGroupFilter instanceGroupFilter, SecurityContext securityContext) {
		return instanceGroupRepository.listAllInstanceGroups(instanceGroupFilter, securityContext);
	}

	public InstanceGroup duplicate(InstanceGroupDuplicate instanceGroupDuplicate, SecurityContext securityContext) {
		InstanceGroup toDuplicate = instanceGroupDuplicate.getInstanceGroup();
		List<InstanceGroupLink> links = instanceGroupLinkService.listAllInstanceGroupLink(new InstanceGroupLinkFilter().setInstanceGroups(Collections.singletonList(toDuplicate)), securityContext);
		InstanceGroupCreate instanceGroupCreate = new InstanceGroupCreate()
				.setExternalId(Optional.ofNullable(toDuplicate.getExternalId()).map(f -> f + "(copy)").orElse(null))
				.setName(toDuplicate.getName() + "(copy)");
		List<Object> toMerge=new ArrayList<>();
		InstanceGroup instanceGroup = createInstanceGroupNoMerge(instanceGroupCreate, securityContext);
		toMerge.add(instanceGroup);
		for (InstanceGroupLink link : links) {
			InstanceGroupLinkCreate instanceGroupLinkCreate = new InstanceGroupLinkCreate()
					.setInstanceGroup(instanceGroup)
					.setBaseclass(link.getBaseclass())
					.setName(link.getName());
			toMerge.add(instanceGroupLinkService.createInstanceGroupLinkNoMerge(instanceGroupLinkCreate, securityContext));
		}
		massMerge(toMerge);
		return instanceGroup;

	}
}
