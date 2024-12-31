package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.PermissionGroupRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupCreate;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupDuplicate;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupFilter;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;



public class PermissionGroupService  implements SegmantixService {

	private final BasicService basicService;
	private final PermissionGroupRepository permissionGroupRepository;
	private final PermissionGroupToBaseclassService permissionGroupToBaseclassService;

    public PermissionGroupService(BasicService basicService, PermissionGroupRepository permissionGroupRepository, PermissionGroupToBaseclassService permissionGroupToBaseclassService) {
        this.basicService = basicService;
        this.permissionGroupRepository = permissionGroupRepository;
        this.permissionGroupToBaseclassService = permissionGroupToBaseclassService;
    }


    public PermissionGroup createPermissionGroup(PermissionGroupCreate permissionGroupCreate, SecurityContext securityContext){
		PermissionGroup permissionGroup= createPermissionGroupNoMerge(permissionGroupCreate,securityContext);
		permissionGroupRepository.merge(permissionGroup);
		return permissionGroup;
	}

	public void merge(Object o){
		permissionGroupRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		permissionGroupRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return permissionGroupRepository.listByIds(c, ids, securityContext);
	}

	public PermissionGroup createPermissionGroupNoMerge(PermissionGroupCreate permissionGroupCreate, SecurityContext securityContext){
		PermissionGroup permissionGroup=new PermissionGroup();
		permissionGroup.setId(UUID.randomUUID().toString());
		updatePermissionGroupNoMerge(permissionGroupCreate,permissionGroup);
		BaseclassService.createSecurityObjectNoMerge(permissionGroup,securityContext);
		return permissionGroup;
	}

	public boolean updatePermissionGroupNoMerge(PermissionGroupCreate permissionGroupCreate, PermissionGroup permissionGroup) {
		boolean update = basicService.updateBasicNoMerge(permissionGroupCreate, permissionGroup);
		if(permissionGroupCreate.getExternalId()!=null&&!permissionGroupCreate.getExternalId().equals(permissionGroup.getExternalId())){
			permissionGroup.setExternalId(permissionGroupCreate.getExternalId());
			update=true;
		}
		return update;
	}

	public PermissionGroup updatePermissionGroup(PermissionGroupUpdate permissionGroupUpdate, SecurityContext securityContext){
		PermissionGroup permissionGroup=permissionGroupUpdate.getPermissionGroup();
		if(updatePermissionGroupNoMerge(permissionGroupUpdate,permissionGroup)){
			permissionGroupRepository.merge(permissionGroup);
		}
		return permissionGroup;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return permissionGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroup> getAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext) {
		List<PermissionGroup> list= listAllPermissionGroups(permissionGroupFilter, securityContext);
		long count=permissionGroupRepository.countAllPermissionGroups(permissionGroupFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupFilter,count);
	}

	public List<PermissionGroup> listAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext) {
		return permissionGroupRepository.listAllPermissionGroups(permissionGroupFilter, securityContext);
	}

	public PermissionGroup duplicate(PermissionGroupDuplicate permissionGroupDuplicate, SecurityContext securityContext) {
		PermissionGroup toDuplicate = permissionGroupDuplicate.getPermissionGroup();
		List<PermissionGroupToBaseclass> links = permissionGroupToBaseclassService.listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setPermissionGroups(Collections.singletonList(toDuplicate)), securityContext);
		PermissionGroupCreate permissionGroupCreate = new PermissionGroupCreate()
				.setExternalId(Optional.ofNullable(toDuplicate.getExternalId()).map(f -> f + "(copy)").orElse(null))
				.setName(toDuplicate.getName() + "(copy)");
		List<Object> toMerge=new ArrayList<>();
		PermissionGroup permissionGroup = createPermissionGroupNoMerge(permissionGroupCreate, securityContext);
		toMerge.add(permissionGroup);
		for (PermissionGroupToBaseclass link : links) {
			PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate = new PermissionGroupToBaseclassCreate()
					.setPermissionGroup(permissionGroup)
					.setSecuredId(link.getSecuredId())
					.setName(link.getName());
			toMerge.add(permissionGroupToBaseclassService.createPermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate, securityContext));
		}
		massMerge(toMerge);
		return permissionGroup;

	}
}
