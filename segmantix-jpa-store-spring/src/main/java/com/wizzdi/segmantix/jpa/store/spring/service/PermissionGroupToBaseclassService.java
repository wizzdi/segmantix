package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.InstanceGroup;
import com.flexicore.model.InstanceGroupLink;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.InstanceGroupLinkRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkMassCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class InstanceGroupLinkService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private InstanceGroupLinkRepository instanceGroupLinkRepository;


	public InstanceGroupLink createInstanceGroupLink(InstanceGroupLinkCreate instanceGroupLinkCreate, SecurityContext securityContext){
		InstanceGroupLink instanceGroupLink= createInstanceGroupLinkNoMerge(instanceGroupLinkCreate,securityContext);
		instanceGroupLinkRepository.merge(instanceGroupLink);
		return instanceGroupLink;
	}
	public void merge(Object o){
		instanceGroupLinkRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		instanceGroupLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return instanceGroupLinkRepository.listByIds(c, ids, securityContext);
	}

	public InstanceGroupLink createInstanceGroupLinkNoMerge(InstanceGroupLinkCreate instanceGroupLinkCreate, SecurityContext securityContext){
		InstanceGroupLink instanceGroupLink=new InstanceGroupLink();
		instanceGroupLink.setId(UUID.randomUUID().toString());
		updateInstanceGroupLinkNoMerge(instanceGroupLinkCreate,instanceGroupLink);
		BaseclassService.createSecurityObjectNoMerge(instanceGroupLink,securityContext);
		return instanceGroupLink;
	}

	public boolean updateInstanceGroupLinkNoMerge(InstanceGroupLinkCreate instanceGroupLinkCreate, InstanceGroupLink instanceGroupLink) {
		boolean update = basicService.updateBasicNoMerge(instanceGroupLinkCreate, instanceGroupLink);
		if(instanceGroupLinkCreate.getBaseclass()!=null&&(instanceGroupLink.getBaseclass()==null||!instanceGroupLinkCreate.getBaseclass().getId().equals(instanceGroupLink.getInstanceGroup().getId()))){
			instanceGroupLink.setBaseclass(instanceGroupLinkCreate.getBaseclass());
			update=true;
		}
		if(instanceGroupLinkCreate.getInstanceGroup()!=null&&(instanceGroupLink.getInstanceGroup()==null||!instanceGroupLinkCreate.getInstanceGroup().getId().equals(instanceGroupLink.getInstanceGroup().getId()))){
			instanceGroupLink.setInstanceGroup(instanceGroupLinkCreate.getInstanceGroup());
			update=true;
		}
		return update;
	}

	public InstanceGroupLink updateInstanceGroupLink(InstanceGroupLinkUpdate instanceGroupLinkUpdate, SecurityContext securityContext){
		InstanceGroupLink instanceGroupLink=instanceGroupLinkUpdate.getInstanceGroupLink();
		if(updateInstanceGroupLinkNoMerge(instanceGroupLinkUpdate,instanceGroupLink)){
			instanceGroupLinkRepository.merge(instanceGroupLink);
		}
		return instanceGroupLink;
	}

	@Deprecated
	public void validate(InstanceGroupLinkCreate instanceGroupLinkCreate, SecurityContext securityContext) {
		basicService.validate(instanceGroupLinkCreate,securityContext);
	}



	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return instanceGroupLinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<InstanceGroupLink> getAllInstanceGroupLink(InstanceGroupLinkFilter instanceGroupLinkFilter, SecurityContext securityContext) {
		List<InstanceGroupLink> list= listAllInstanceGroupLink(instanceGroupLinkFilter, securityContext);
		long count=instanceGroupLinkRepository.countAllInstanceGroupLinks(instanceGroupLinkFilter,securityContext);
		return new PaginationResponse<>(list,instanceGroupLinkFilter,count);
	}

	public List<InstanceGroupLink> listAllInstanceGroupLink(InstanceGroupLinkFilter instanceGroupLinkFilter, SecurityContext securityContext) {
		return instanceGroupLinkRepository.listAllInstanceGroupLinks(instanceGroupLinkFilter, securityContext);
	}

	public Map<String, Map<String, InstanceGroupLink>> massCreatePermissionLinks(InstanceGroupLinkMassCreate instanceGroupLinkMassCreate, SecurityContext securityContext) {
		List<InstanceGroup> instanceGroups = instanceGroupLinkMassCreate.getInstanceGroups();
		List<Baseclass> baseclasses = instanceGroupLinkMassCreate.getBaseclasses();
		Map<String,Map<String, InstanceGroupLink>> permissionLinks = baseclasses.isEmpty()||instanceGroups.isEmpty()?new HashMap<>():listAllInstanceGroupLink(new InstanceGroupLinkFilter().setInstanceGroups(new ArrayList<>(instanceGroups)).setBaseclasses(baseclasses), securityContext).stream().collect(Collectors.groupingBy(f->f.getInstanceGroup().getId(),Collectors.toMap(f -> f.getBaseclass().getId(), f -> f, (a, b) -> a)));
		for (InstanceGroup instanceGroup : instanceGroups) {
			Map<String, InstanceGroupLink> instanceGroupLinks = permissionLinks.computeIfAbsent(instanceGroup.getId(), f -> new HashMap<>());
			for (Baseclass baseclass : baseclasses) {
				InstanceGroupLink existing=instanceGroupLinks.get(baseclass.getId());
				if(existing==null){
					InstanceGroupLink instanceGroupLink = createInstanceGroupLink(new InstanceGroupLinkCreate().setBaseclass(baseclass).setInstanceGroup(instanceGroup), securityContext);
					instanceGroupLinks.put(instanceGroupLink.getBaseclass().getId(),instanceGroupLink);
				}
			}
		}

		return permissionLinks;
	}
}
