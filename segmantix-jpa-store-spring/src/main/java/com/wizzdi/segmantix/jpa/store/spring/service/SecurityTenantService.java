package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Tenant;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.TenantRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class TenantService  {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private TenantRepository tenantRepository;


	public Tenant createTenant(TenantCreate tenantCreate, SecurityContext securityContext){
		Tenant tenant= createTenantNoMerge(tenantCreate,securityContext);
		tenantRepository.merge(tenant);
		return tenant;
	}

	public <T> T merge(T o){
		return tenantRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return tenantRepository.listByIds(c, ids, securityContext);
	}

	public Tenant createTenantNoMerge(TenantCreate tenantCreate, SecurityContext securityContext){
		Tenant tenant=new Tenant();
		tenant.setId(UUID.randomUUID().toString());
		updateTenantNoMerge(tenantCreate,tenant);
		BaseclassService.createSecurityObjectNoMerge(tenant,securityContext);
		return tenant;
	}

	public boolean updateTenantNoMerge(TenantCreate tenantCreate, Tenant tenant) {
		return securityEntityService.updateNoMerge(tenantCreate,tenant);
	}

	public Tenant updateTenant(TenantUpdate tenantUpdate, SecurityContext securityContext){
		Tenant tenant=tenantUpdate.getTenantToUpdate();
		if(updateTenantNoMerge(tenantUpdate,tenant)){
			tenantRepository.merge(tenant);
		}
		return tenant;
	}



	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return tenantRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Tenant> getAllTenants(TenantFilter tenantFilter, SecurityContext securityContext) {
		List<Tenant> list= listAllTenants(tenantFilter, securityContext);
		long count=tenantRepository.countAllTenants(tenantFilter,securityContext);
		return new PaginationResponse<>(list,tenantFilter,count);
	}

	public List<Tenant> listAllTenants(TenantFilter tenantFilter, SecurityContext securityContext) {
		return tenantRepository.listAllTenants(tenantFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tenantRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tenantRepository.findByIdOrNull(type, id);
	}
}
