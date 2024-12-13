package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.TenantSecurityRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class TenantSecurityService  {

	@Autowired
	private SecurityService securityService;
	@Autowired
	private TenantSecurityRepository tenantSecurityRepository;


	public TenantSecurity createTenantSecurity(TenantSecurityCreate tenantSecurityCreate, SecurityContext securityContext){
		TenantSecurity tenantSecurity= createTenantSecurityNoMerge(tenantSecurityCreate,securityContext);
		tenantSecurityRepository.merge(tenantSecurity);
		return tenantSecurity;
	}

	public void merge(Object o){
		tenantSecurityRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantSecurityRepository.massMerge(list);
	}
	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return tenantSecurityRepository.listByIds(c, ids, securityContext);
	}

	public TenantSecurity createTenantSecurityNoMerge(TenantSecurityCreate tenantSecurityCreate, SecurityContext securityContext){
		TenantSecurity tenantSecurity=new TenantSecurity();
		tenantSecurity.setId(UUID.randomUUID().toString());
		updateTenantSecurityNoMerge(tenantSecurityCreate,tenantSecurity);
		BaseclassService.createSecurityObjectNoMerge(tenantSecurity,securityContext);
		return tenantSecurity;
	}

	public boolean updateTenantSecurityNoMerge(TenantSecurityCreate tenantSecurityCreate, TenantSecurity tenantSecurity) {
		boolean update = securityService.updateSecurityNoMerge(tenantSecurityCreate, tenantSecurity);
		if(tenantSecurityCreate.getTenant()!=null&&(tenantSecurity.getTenant()==null||!tenantSecurityCreate.getTenant().getId().equals(tenantSecurity.getTenant().getId()))){
			tenantSecurity.setTenant(tenantSecurityCreate.getTenant());
			update=true;
		}
		return update;
	}

	public TenantSecurity updateTenantSecurity(TenantSecurityUpdate tenantSecurityUpdate, SecurityContext securityContext){
		TenantSecurity tenantSecurity=tenantSecurityUpdate.getTenantSecurity();
		if(updateTenantSecurityNoMerge(tenantSecurityUpdate,tenantSecurity)){
			tenantSecurityRepository.merge(tenantSecurity);
		}
		return tenantSecurity;
	}


	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return tenantSecurityRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantSecurity> getAllTenantSecuritys(TenantSecurityFilter tenantSecurityFilter, SecurityContext securityContext) {
		List<TenantSecurity> list= listAllTenantSecuritys(tenantSecurityFilter, securityContext);
		long count= tenantSecurityRepository.countAllTenantSecuritys(tenantSecurityFilter,securityContext);
		return new PaginationResponse<>(list, tenantSecurityFilter,count);
	}

	public List<TenantSecurity> listAllTenantSecuritys(TenantSecurityFilter tenantSecurityFilter, SecurityContext securityContext) {
		return tenantSecurityRepository.listAllTenantSecuritys(tenantSecurityFilter, securityContext);
	}
}
