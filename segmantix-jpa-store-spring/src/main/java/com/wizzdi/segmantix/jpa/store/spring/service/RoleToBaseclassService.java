package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.RoleSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.RoleSecurityRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleSecurityCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleSecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleSecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class RoleSecurityService  {

	@Autowired
	private SecurityService securityService;
	@Autowired
	private RoleSecurityRepository roleSecurityRepository;


	public RoleSecurity createRoleSecurity(RoleSecurityCreate roleSecurityCreate, SecurityContext securityContext){
		RoleSecurity roleSecurity= createRoleSecurityNoMerge(roleSecurityCreate,securityContext);
		roleSecurityRepository.merge(roleSecurity);
		return roleSecurity;
	}
	public void merge(Object o){
		roleSecurityRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		roleSecurityRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return roleSecurityRepository.listByIds(c, ids, securityContext);
	}


	public RoleSecurity createRoleSecurityNoMerge(RoleSecurityCreate roleSecurityCreate, SecurityContext securityContext){
		RoleSecurity roleSecurity=new RoleSecurity();
		roleSecurity.setId(UUID.randomUUID().toString());
		updateRoleSecurityNoMerge(roleSecurityCreate,roleSecurity);
		BaseclassService.createSecurityObjectNoMerge(roleSecurity,securityContext);
		return roleSecurity;
	}

	public boolean updateRoleSecurityNoMerge(RoleSecurityCreate roleSecurityCreate, RoleSecurity roleSecurity) {
		boolean update = securityService.updateSecurityNoMerge(roleSecurityCreate, roleSecurity);
		if(roleSecurityCreate.getRole()!=null&&(roleSecurity.getRole()==null||!roleSecurityCreate.getRole().getId().equals(roleSecurity.getRole().getId()))){
			roleSecurity.setRole(roleSecurityCreate.getRole());
			update=true;
		}
		return update;
	}

	public RoleSecurity updateRoleSecurity(RoleSecurityUpdate roleSecurityUpdate, SecurityContext securityContext){
		RoleSecurity roleSecurity=roleSecurityUpdate.getRoleSecurity();
		if(updateRoleSecurityNoMerge(roleSecurityUpdate,roleSecurity)){
			roleSecurityRepository.merge(roleSecurity);
		}
		return roleSecurity;
	}


	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return roleSecurityRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<RoleSecurity> getAllRoleSecurity(RoleSecurityFilter roleSecurityFilter, SecurityContext securityContext) {
		List<RoleSecurity> list= listAllRoleSecuritys(roleSecurityFilter, securityContext);
		long count=roleSecurityRepository.countAllRoleSecuritys(roleSecurityFilter,securityContext);
		return new PaginationResponse<>(list,roleSecurityFilter,count);
	}

	public List<RoleSecurity> listAllRoleSecuritys(RoleSecurityFilter roleSecurityFilter, SecurityContext securityContext) {
		return roleSecurityRepository.listAllRoleSecuritys(roleSecurityFilter, securityContext);
	}
}
