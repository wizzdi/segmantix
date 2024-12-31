package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.SecurityTenantRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class SecurityTenantService  implements SegmantixService {

	
	private final SecurityEntityService securityEntityService;
	private final SecurityTenantRepository tenantRepository;

	public SecurityTenantService(SecurityEntityService securityEntityService, SecurityTenantRepository tenantRepository) {
		this.securityEntityService = securityEntityService;
		this.tenantRepository = tenantRepository;
	}

	public SecurityTenant createTenant(SecurityTenantCreate tenantCreate, SecurityContext securityContext){
		SecurityTenant tenant= createTenantNoMerge(tenantCreate,securityContext);
		tenantRepository.merge(tenant);
		return tenant;
	}

	public <T> T merge(T o){
		return tenantRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return tenantRepository.listByIds(c, ids, securityContext);
	}

	public SecurityTenant createTenantNoMerge(SecurityTenantCreate tenantCreate, SecurityContext securityContext){
		SecurityTenant tenant=new SecurityTenant();
		tenant.setId(UUID.randomUUID().toString());
		updateTenantNoMerge(tenantCreate,tenant);
		BaseclassService.createSecurityObjectNoMerge(tenant,securityContext);
		return tenant;
	}

	public boolean updateTenantNoMerge(SecurityTenantCreate tenantCreate, SecurityTenant tenant) {
		return securityEntityService.updateNoMerge(tenantCreate,tenant);
	}

	public SecurityTenant updateTenant(SecurityTenantUpdate tenantUpdate, SecurityContext securityContext){
		SecurityTenant tenant=tenantUpdate.getTenantToUpdate();
		if(updateTenantNoMerge(tenantUpdate,tenant)){
			tenantRepository.merge(tenant);
		}
		return tenant;
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return tenantRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityTenant> getAllTenants(SecurityTenantFilter tenantFilter, SecurityContext securityContext) {
		List<SecurityTenant> list= listAllTenants(tenantFilter, securityContext);
		long count=tenantRepository.countAllTenants(tenantFilter,securityContext);
		return new PaginationResponse<>(list,tenantFilter,count);
	}

	public List<SecurityTenant> listAllTenants(SecurityTenantFilter tenantFilter, SecurityContext securityContext) {
		return tenantRepository.listAllTenants(tenantFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tenantRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tenantRepository.findByIdOrNull(type, id);
	}
}
