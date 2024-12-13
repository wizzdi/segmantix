package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.Tenant;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.RoleRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Component
public class RoleService  {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private RoleRepository roleRepository;


	public Role createRole(RoleCreate roleCreate, SecurityContext securityContext){
		Role role= createRoleNoMerge(roleCreate,securityContext);
		roleRepository.merge(role);
		return role;
	}

	public <T> T merge(T o){
		return roleRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		roleRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return roleRepository.listByIds(c, ids, securityContext);
	}


	public Role createRoleNoMerge(RoleCreate roleCreate, SecurityContext securityContext){
		Role role=new Role();
		role.setId(UUID.randomUUID().toString());
		updateRoleNoMerge(roleCreate,role);
		BaseclassService.createSecurityObjectNoMerge(role,securityContext);
		role.getSecurity().setTenant(roleCreate.getTenant());
		return role;
	}

	public boolean updateRoleNoMerge(RoleCreate roleCreate, Role role) {
		boolean update = securityEntityService.updateNoMerge(roleCreate, role);
		Tenant currentTenant=Optional.of(role).map(f->f.getSecurity()).map(f->f.getTenant()).orElse(null);
		if(roleCreate.getTenant()!=null&&role.getSecurity()!=null&&(currentTenant==null||!roleCreate.getTenant().getId().equals(currentTenant.getId()))){
			role.getSecurity().setTenant(roleCreate.getTenant());
			update=true;
		}
		return update;
	}

	public Role updateRole(RoleUpdate roleUpdate, SecurityContext securityContext){
		Role role=roleUpdate.getRole();
		if(updateRoleNoMerge(roleUpdate,role)){
			roleRepository.merge(role);
		}
		return role;
	}



	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return roleRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Role> getAllRoles(RoleFilter roleFilter, SecurityContext securityContext) {
		List<Role> list= listAllRoles(roleFilter, securityContext);
		long count=roleRepository.countAllRoles(roleFilter,securityContext);
		return new PaginationResponse<>(list,roleFilter,count);
	}

	public List<Role> listAllRoles(RoleFilter roleFilter, SecurityContext securityContext) {
		return roleRepository.listAllRoles(roleFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return roleRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return roleRepository.findByIdOrNull(type, id);
	}
}
