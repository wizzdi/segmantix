package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.PermissionGroupToBaseclassRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassMassCreate;
import com.wizzdi.segmantix.store.jpa.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.request.SecuredHolder;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;



public class PermissionGroupToBaseclassService  implements SegmantixService {

	private final BasicService basicService;
	private final PermissionGroupToBaseclassRepository permissionGroupToBaseclassRepository;

	public PermissionGroupToBaseclassService(BasicService basicService, PermissionGroupToBaseclassRepository permissionGroupToBaseclassRepository) {
		this.basicService = basicService;
		this.permissionGroupToBaseclassRepository = permissionGroupToBaseclassRepository;
	}



	public PermissionGroupToBaseclass createPermissionGroupToBaseclass(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass= createPermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,securityContext);
		permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		return permissionGroupToBaseclass;
	}
	public void merge(Object o){
		permissionGroupToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		permissionGroupToBaseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public PermissionGroupToBaseclass createPermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=new PermissionGroupToBaseclass();
		permissionGroupToBaseclass.setId(UUID.randomUUID().toString());
		updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,permissionGroupToBaseclass);
		BaseclassService.createSecurityObjectNoMerge(permissionGroupToBaseclass,securityContext);
		return permissionGroupToBaseclass;
	}

	public boolean updatePermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, PermissionGroupToBaseclass permissionGroupToBaseclass) {
		boolean update = basicService.updateBasicNoMerge(permissionGroupToBaseclassCreate, permissionGroupToBaseclass);
		if(permissionGroupToBaseclassCreate.getSecuredId()!=null&&!permissionGroupToBaseclassCreate.getSecuredId().equals(permissionGroupToBaseclass.getSecuredId())){
			permissionGroupToBaseclass.setSecuredId(permissionGroupToBaseclassCreate.getSecuredId());
			update=true;
		}

		if(permissionGroupToBaseclassCreate.getSecuredType()!=null&&!permissionGroupToBaseclassCreate.getSecuredType().name().equals(permissionGroupToBaseclass.getSecuredType())){
			permissionGroupToBaseclass.setSecuredType(permissionGroupToBaseclassCreate.getSecuredType().name());
			update=true;
		}

		if(permissionGroupToBaseclassCreate.getSecuredCreationDate()!=null&&!permissionGroupToBaseclassCreate.getSecuredCreationDate().equals(permissionGroupToBaseclass.getSecuredCreationDate())){
			permissionGroupToBaseclass.setSecuredCreationDate(permissionGroupToBaseclassCreate.getSecuredCreationDate());
			update=true;
		}
		if(permissionGroupToBaseclassCreate.getPermissionGroup()!=null&&(permissionGroupToBaseclass.getPermissionGroup()==null||!permissionGroupToBaseclassCreate.getPermissionGroup().getId().equals(permissionGroupToBaseclass.getPermissionGroup().getId()))){
			permissionGroupToBaseclass.setPermissionGroup(permissionGroupToBaseclassCreate.getPermissionGroup());
			update=true;
		}
		return update;
	}

	public PermissionGroupToBaseclass updatePermissionGroupToBaseclass(PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=permissionGroupToBaseclassUpdate.getPermissionGroupToBaseclass();
		if(updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassUpdate,permissionGroupToBaseclass)){
			permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		}
		return permissionGroupToBaseclass;
	}




	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroupToBaseclass> getAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		List<PermissionGroupToBaseclass> list= listAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter, securityContext);
		long count=permissionGroupToBaseclassRepository.countAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupToBaseclassFilter,count);
	}

	public List<PermissionGroupToBaseclass> listAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.listAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter, securityContext);
	}

	public Map<String, Map<String, PermissionGroupToBaseclass>> massCreatePermissionLinks(PermissionGroupToBaseclassMassCreate permissionGroupToBaseclassMassCreate, SecurityContext securityContext) {
		List<PermissionGroup> permissionGroups = permissionGroupToBaseclassMassCreate.getPermissionGroups();
	List<SecuredHolder> baseclasses = permissionGroupToBaseclassMassCreate.getSecuredHolders();
	Set<String> baseclassIds=baseclasses.stream().map(f->f.id()).collect(Collectors.toSet());
	List<Clazz> baseclassTypes=baseclasses.stream().map(f->f.type()).collect(Collectors.toList());
		Map<String,Map<String, PermissionGroupToBaseclass>> permissionLinks = baseclasses.isEmpty()||permissionGroups.isEmpty()?new HashMap<>():listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setPermissionGroups(new ArrayList<>(permissionGroups)).setSecuredIds(baseclassIds).setClazzes(baseclassTypes), securityContext).stream().collect(Collectors.groupingBy(f->f.getPermissionGroup().getId(),Collectors.toMap(f -> f.getSecuredId(), f -> f, (a, b) -> a)));
		for (PermissionGroup permissionGroup : permissionGroups) {
			Map<String, PermissionGroupToBaseclass> permissionGroupLinks = permissionLinks.computeIfAbsent(permissionGroup.getId(), f -> new HashMap<>());
			for (SecuredHolder baseclass : baseclasses) {
				PermissionGroupToBaseclass existing=permissionGroupLinks.get(baseclass.id());
				if(existing==null){
					PermissionGroupToBaseclass permissionGroupToBaseclass = createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setSecuredId(baseclass.id()).setSecuredType(baseclass.type()).setPermissionGroup(permissionGroup), securityContext);
					permissionGroupLinks.put(permissionGroupToBaseclass.getSecuredId(),permissionGroupToBaseclass);
				}
			}
		}

		return permissionLinks;
	}
}
