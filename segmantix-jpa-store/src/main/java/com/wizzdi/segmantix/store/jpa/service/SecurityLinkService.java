package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.SecurityLinkRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.SecurityLink;
import com.wizzdi.segmantix.store.jpa.request.RoleFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityLinkUpdate;
import com.wizzdi.segmantix.store.jpa.request.SecurityTenantFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Set;



public class SecurityLinkService  implements SegmantixService {

	
	private final BasicService basicService;
	private final SecurityLinkRepository securityLinkRepository;
	private final SecurityTenantService securityTenantService;
	private final RoleService roleService;

	public SecurityLinkService(BasicService basicService, SecurityLinkRepository securityLinkRepository, SecurityTenantService securityTenantService, RoleService roleService) {
		this.basicService = basicService;
		this.securityLinkRepository = securityLinkRepository;
		this.securityTenantService = securityTenantService;
		this.roleService = roleService;
	}

	public void merge(Object o){
		securityLinkRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityLink securityLink) {
		boolean updated = basicService.updateBasicNoMerge(securityLinkCreate, securityLink);
		if(securityLinkCreate.getAccess()!=null&&!securityLinkCreate.getAccess().equals(securityLink.getAccess())){
			securityLink.setAccess(securityLinkCreate.getAccess());
			updated=true;
		}
		if(securityLinkCreate.getOperation()!=null&&!securityLinkCreate.getOperation().getId().equals(securityLink.getOperationId())){
			securityLink.setOperationId(securityLinkCreate.getOperation().getId());
			updated=true;
		}
		if(securityLinkCreate.getOperationGroup()!=null&&(securityLink.getOperationGroup()==null||!securityLinkCreate.getOperationGroup().getId().equals(securityLink.getOperationGroup().getId()))){
			securityLink.setOperationGroup(securityLinkCreate.getOperationGroup());
			updated=true;
		}
		if (securityLinkCreate.getSecuredId() != null && !securityLinkCreate.getSecuredId().equals(securityLink.getSecuredId())) {
			securityLink.setSecuredId(securityLinkCreate.getSecuredId());
			updated = true;
		}
		if(securityLinkCreate.getPermissionGroup()!=null&&(securityLink.getPermissionGroup()==null || !securityLinkCreate.getPermissionGroup().getId().equals(securityLink.getPermissionGroup().getId()))){
			securityLink.setPermissionGroup(securityLinkCreate.getPermissionGroup());
			updated=true;
		}

		if(securityLinkCreate.getClazz()!=null&&!securityLinkCreate.getClazz().name().equals(securityLink.getSecuredType())){
			securityLink.setSecuredType(securityLinkCreate.getClazz().name());
			updated=true;
		}
		if(securityLinkCreate.getSecurityLinkGroup()!=null&&(securityLink.getSecurityLinkGroup()==null || !securityLinkCreate.getSecurityLinkGroup().getId().equals(securityLink.getSecurityLinkGroup().getId()))){
			securityLink.setSecurityLinkGroup(securityLinkCreate.getSecurityLinkGroup());
			updated=true;
		}
		return updated;
	}

	public SecurityLink updateSecurityLink(SecurityLinkUpdate securityLinkUpdate, SecurityContext securityContext) {
		SecurityLink securityLink=securityLinkUpdate.getSecurityLink();
		if(updateSecurityLinkNoMerge(securityLinkUpdate,securityLink)){
			securityLinkRepository.merge(securityLink);
		}
		return securityLink;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityLinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityLink> getAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		List<SecurityLink> list= listAllSecurityLinks(securityLinkFilter, securityContext);
		long count=securityLinkRepository.countAllSecurityLinks(securityLinkFilter,securityContext);
		return new PaginationResponse<>(list,securityLinkFilter,count);
	}

	public List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		return securityLinkRepository.listAllSecurityLinks(securityLinkFilter, securityContext);
	}

	public void setRelevant(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		if(securityLinkFilter.getRelevantUsers()!=null&&!securityLinkFilter.getRelevantUsers().isEmpty()){
			securityLinkFilter.setRelevantRoles(roleService.listAllRoles(new RoleFilter().setUsers(securityLinkFilter.getRelevantUsers()),securityContext));
			securityLinkFilter.setRelevantTenants(securityTenantService.listAllTenants(new SecurityTenantFilter().setUsers(securityLinkFilter.getRelevantUsers()),securityContext));
		}
	}
}
