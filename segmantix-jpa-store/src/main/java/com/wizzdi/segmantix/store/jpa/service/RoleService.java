package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.RoleRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Role;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.request.RoleCreate;
import com.wizzdi.segmantix.store.jpa.request.RoleFilter;
import com.wizzdi.segmantix.store.jpa.request.RoleUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;



public class RoleService  implements SegmantixService {

	private final SecurityEntityService securityEntityService;
	private final RoleRepository roleRepository;

    public RoleService(SecurityEntityService securityEntityService, RoleRepository roleRepository) {
        this.securityEntityService = securityEntityService;
        this.roleRepository = roleRepository;
    }


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

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return roleRepository.listByIds(c, ids, securityContext);
	}


	public Role createRoleNoMerge(RoleCreate roleCreate, SecurityContext securityContext){
		Role role=new Role();
		role.setId(UUID.randomUUID().toString());
		updateRoleNoMerge(roleCreate,role);
		BaseclassService.createSecurityObjectNoMerge(role,securityContext);
		return role;
	}

	public boolean updateRoleNoMerge(RoleCreate roleCreate, Role role) {
		boolean update = securityEntityService.updateNoMerge(roleCreate, role);
		SecurityTenant currentTenant=Optional.of(role).map(f->f.getTenant()).orElse(null);
		if(roleCreate.getTenant()!=null&&(currentTenant==null||!roleCreate.getTenant().getId().equals(currentTenant.getId()))){
			role.setTenant(roleCreate.getTenant());
			update=true;
		}
		if(roleCreate.getSuperAdmin()!=null&&!roleCreate.getSuperAdmin().equals(role.isSuperAdmin())){
			role.setSuperAdmin(roleCreate.getSuperAdmin());
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



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
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
