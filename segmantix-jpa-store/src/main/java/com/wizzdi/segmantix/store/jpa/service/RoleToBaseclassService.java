package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.RoleToBaseclassRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass;
import com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassCreate;
import com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.request.RoleToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class RoleToBaseclassService  implements SegmantixService {

	private final SecurityLinkService securityLinkService;
	private final RoleToBaseclassRepository roleToBaseclassRepository;

	public RoleToBaseclassService(SecurityLinkService securityLinkService, RoleToBaseclassRepository roleToBaseclassRepository) {
		this.securityLinkService = securityLinkService;
		this.roleToBaseclassRepository = roleToBaseclassRepository;
	}

	public RoleToBaseclass createRoleToBaseclass(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContext securityContext){
		RoleToBaseclass roleToBaseclass= createRoleToBaseclassNoMerge(roleToBaseclassCreate,securityContext);
		roleToBaseclassRepository.merge(roleToBaseclass);
		return roleToBaseclass;
	}
	public void merge(Object o){
		roleToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		roleToBaseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return roleToBaseclassRepository.listByIds(c, ids, securityContext);
	}


	public RoleToBaseclass createRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContext securityContext){
		RoleToBaseclass roleToBaseclass=new RoleToBaseclass();
		roleToBaseclass.setId(UUID.randomUUID().toString());
		updateRoleToBaseclassNoMerge(roleToBaseclassCreate,roleToBaseclass);
		BaseclassService.createSecurityObjectNoMerge(roleToBaseclass,securityContext);
		return roleToBaseclass;
	}

	public boolean updateRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, RoleToBaseclass roleToBaseclass) {
		boolean update = securityLinkService.updateSecurityLinkNoMerge(roleToBaseclassCreate, roleToBaseclass);
		if(roleToBaseclassCreate.getRole()!=null&&(roleToBaseclass.getRole()==null||!roleToBaseclassCreate.getRole().getId().equals(roleToBaseclass.getRole().getId()))){
			roleToBaseclass.setRole(roleToBaseclassCreate.getRole());
			update=true;
		}
		return update;
	}

	public RoleToBaseclass updateRoleToBaseclass(RoleToBaseclassUpdate roleToBaseclassUpdate, SecurityContext securityContext){
		RoleToBaseclass roleToBaseclass=roleToBaseclassUpdate.getRoleToBaseclass();
		if(updateRoleToBaseclassNoMerge(roleToBaseclassUpdate,roleToBaseclass)){
			roleToBaseclassRepository.merge(roleToBaseclass);
		}
		return roleToBaseclass;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return roleToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<RoleToBaseclass> getAllRoleToBaseclass(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext) {
		List<RoleToBaseclass> list= listAllRoleToBaseclasss(roleToBaseclassFilter, securityContext);
		long count=roleToBaseclassRepository.countAllRoleToBaseclasss(roleToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,roleToBaseclassFilter,count);
	}

	public List<RoleToBaseclass> listAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext) {
		return roleToBaseclassRepository.listAllRoleToBaseclasss(roleToBaseclassFilter, securityContext);
	}
}
