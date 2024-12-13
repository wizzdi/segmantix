package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Security;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.SecurityApiRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.*;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public class SecurityService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityApiRepository securityApiRepository;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private RoleService roleService;



	public void merge(Object o){
		securityApiRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityApiRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return securityApiRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityNoMerge(SecurityCreate securityCreate, Security security) {
		boolean updated = basicService.updateBasicNoMerge(securityCreate, security);
		if(securityCreate.getAccess()!=null&&!securityCreate.getAccess().equals(security.getAccess())){
			security.setAccess(securityCreate.getAccess());
			updated=true;
		}
		if(securityCreate.getOperation()!=null&&(security.getOperation()==null||!securityCreate.getOperation().getId().equals(security.getOperation().getId()))){
			security.setOperation(securityCreate.getOperation());
			updated=true;
		}
		if(securityCreate.getOperationGroup()!=null&&(security.getOperationGroup()==null||!securityCreate.getOperationGroup().getId().equals(security.getOperationGroup().getId()))){
			security.setOperationGroup(securityCreate.getOperationGroup());
			updated=true;
		}
		if(securityCreate.getBaseclass()!=null&&(security.getBaseclass()==null || !securityCreate.getBaseclass().getId().equals(security.getBaseclass().getId()))){
			security.setBaseclass(securityCreate.getBaseclass());
			updated=true;
		}

		if(securityCreate.getInstanceGroup()!=null&&(security.getInstanceGroup()==null || !securityCreate.getInstanceGroup().getId().equals(security.getInstanceGroup().getId()))){
			security.setInstanceGroup(securityCreate.getInstanceGroup());
			updated=true;
		}

		if(securityCreate.getClazz()!=null&&(security.getClazz()==null || !securityCreate.getClazz().getId().equals(security.getClazz().getId()))){
			security.setClazz(securityCreate.getClazz());
			updated=true;
		}
		if(securityCreate.getSecurityGroup()!=null&&(security.getSecurityGroup()==null || !securityCreate.getSecurityGroup().getId().equals(security.getSecurityGroup().getId()))){
			security.setSecurityGroup(securityCreate.getSecurityGroup());
			updated=true;
		}
		return updated;
	}

	public Security updateSecurity(SecurityUpdate securityUpdate, SecurityContext securityContext){
		Security security=securityUpdate.getSecurity();
		if(updateSecurityNoMerge(securityUpdate,security)){
			securityApiRepository.merge(security);
		}
		return security;
	}


	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return securityApiRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Security> getAllSecuritys(SecurityFilter securityFilter, SecurityContext securityContext) {
		List<Security> list= listAllSecuritys(securityFilter, securityContext);
		long count= securityApiRepository.countAllSecuritys(securityFilter,securityContext);
		return new PaginationResponse<>(list,securityFilter,count);
	}

	public List<Security> listAllSecuritys(SecurityFilter securityFilter, SecurityContext securityContext) {
		return securityApiRepository.listAllSecuritys(securityFilter, securityContext);
	}

	public void setRelevant(SecurityFilter securityFilter, SecurityContext securityContext) {
		if(securityFilter.getRelevantUsers()!=null&&!securityFilter.getRelevantUsers().isEmpty()){
			securityFilter.setRelevantRoles(roleService.listAllRoles(new RoleFilter().setUsers(securityFilter.getRelevantUsers()),securityContext));
			securityFilter.setRelevantTenants(tenantService.listAllTenants(new TenantFilter().setUsers(securityFilter.getRelevantUsers()),securityContext));
		}
	}
}
