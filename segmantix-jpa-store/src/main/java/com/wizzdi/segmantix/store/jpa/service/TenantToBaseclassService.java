package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.TenantToBaseclassRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass;
import com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassCreate;
import com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.request.TenantToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class TenantToBaseclassService  implements SegmantixService {

	
	private final SecurityLinkService securityLinkService;
	private final TenantToBaseclassRepository tenantToBaseclassRepository;

	public TenantToBaseclassService(SecurityLinkService securityLinkService, TenantToBaseclassRepository tenantToBaseclassRepository) {
		this.securityLinkService = securityLinkService;
		this.tenantToBaseclassRepository = tenantToBaseclassRepository;
	}

	public TenantToBaseclass createTenantToBaseclass(TenantToBaseclassCreate tenantToBaseclassCreate, SecurityContext securityContext){
		TenantToBaseclass tenantToBaseclass= createTenantToBaseclassNoMerge(tenantToBaseclassCreate,securityContext);
		tenantToBaseclassRepository.merge(tenantToBaseclass);
		return tenantToBaseclass;
	}

	public void merge(Object o){
		tenantToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantToBaseclassRepository.massMerge(list);
	}
	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return tenantToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public TenantToBaseclass createTenantToBaseclassNoMerge(TenantToBaseclassCreate tenantToBaseclassCreate, SecurityContext securityContext){
		TenantToBaseclass tenantToBaseclass=new TenantToBaseclass();
		tenantToBaseclass.setId(UUID.randomUUID().toString());
		updateTenantToBaseclassNoMerge(tenantToBaseclassCreate,tenantToBaseclass);
		createSecurityObjectNoMerge(tenantToBaseclass,securityContext);
		return tenantToBaseclass;
	}

	public boolean updateTenantToBaseclassNoMerge(TenantToBaseclassCreate tenantToBaseclassCreate, TenantToBaseclass tenantToBaseclass) {
		boolean update = securityLinkService.updateSecurityLinkNoMerge(tenantToBaseclassCreate, tenantToBaseclass);
		if(tenantToBaseclassCreate.getTenant()!=null&&(tenantToBaseclass.getTenant()==null||!tenantToBaseclassCreate.getTenant().getId().equals(tenantToBaseclass.getTenant().getId()))){
			tenantToBaseclass.setTenant(tenantToBaseclassCreate.getTenant());
			update=true;
		}
		return update;
	}

	public TenantToBaseclass updateTenantToBaseclass(TenantToBaseclassUpdate tenantToBaseclassUpdate, SecurityContext securityContext){
		TenantToBaseclass tenantToBaseclass=tenantToBaseclassUpdate.getTenantToBaseclass();
		if(updateTenantToBaseclassNoMerge(tenantToBaseclassUpdate,tenantToBaseclass)){
			tenantToBaseclassRepository.merge(tenantToBaseclass);
		}
		return tenantToBaseclass;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return tenantToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantToBaseclass> getAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContext securityContext) {
		List<TenantToBaseclass> list= listAllTenantToBaseclasss(tenantToBaseclassFilter, securityContext);
		long count= tenantToBaseclassRepository.countAllTenantToBaseclasss(tenantToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list, tenantToBaseclassFilter,count);
	}

	public List<TenantToBaseclass> listAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContext securityContext) {
		return tenantToBaseclassRepository.listAllTenantToBaseclasss(tenantToBaseclassFilter, securityContext);
	}

	public static <T extends Baseclass> Baseclass createSecurityObjectNoMerge(T subject, SecurityContext securityContext) {
		subject.setSecurityId(subject.getId());
		if(securityContext==null){
			return subject;
		}
		subject.setCreator(securityContext.getUser());
		//TODO:clazz?
		return subject;
	}
}
