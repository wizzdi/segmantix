package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.TenantToUserRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.store.jpa.request.TenantToUserCreate;
import com.wizzdi.segmantix.store.jpa.request.TenantToUserFilter;
import com.wizzdi.segmantix.store.jpa.request.TenantToUserUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class TenantToUserService  implements SegmantixService {

	
	private final BasicService basicService;
	private final TenantToUserRepository tenantToUserRepository;

	public TenantToUserService(BasicService basicService, TenantToUserRepository tenantToUserRepository) {
		this.basicService = basicService;
		this.tenantToUserRepository = tenantToUserRepository;
	}

	public TenantToUser createTenantToUser(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext){
		TenantToUser tenantToUser= createTenantToUserNoMerge(tenantToUserCreate,securityContext);
		tenantToUserRepository.merge(tenantToUser);
		return tenantToUser;
	}

	public <T> T merge(T o){
		return tenantToUserRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantToUserRepository.massMerge(list);
	}
	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return tenantToUserRepository.listByIds(c, ids, securityContext);
	}

	public TenantToUser createTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext){
		TenantToUser tenantToUser=new TenantToUser();
		tenantToUser.setId(UUID.randomUUID().toString());
		updateTenantToUserNoMerge(tenantToUserCreate,tenantToUser);
		createSecurityObjectNoMerge(tenantToUser,securityContext);
		return tenantToUser;
	}

	public boolean updateTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, TenantToUser tenantToUser) {
		boolean update = basicService.updateBasicNoMerge(tenantToUserCreate, tenantToUser);
		if(tenantToUserCreate.getUser()!=null&&(tenantToUser.getUser()==null||!tenantToUserCreate.getUser().getId().equals(tenantToUser.getUser().getId()))){
			tenantToUser.setUser(tenantToUserCreate.getUser());
			update=true;
		}
		if(tenantToUserCreate.getTenant()!=null&&(tenantToUser.getTenant()==null||!tenantToUserCreate.getTenant().getId().equals(tenantToUser.getTenant().getId()))){
			tenantToUser.setTenant(tenantToUserCreate.getTenant());
			update=true;
		}

		if(tenantToUserCreate.getDefaultTenant()!=null&&!tenantToUserCreate.getDefaultTenant().equals(tenantToUser.isDefaultTenant())){
			tenantToUser.setDefaultTenant(tenantToUserCreate.getDefaultTenant());
			update=true;
		}
		return update;
	}

	public TenantToUser updateTenantToUser(TenantToUserUpdate tenantToUserUpdate, SecurityContext securityContext){
		TenantToUser tenantToUser=tenantToUserUpdate.getTenantToUser();
		if(updateTenantToUserNoMerge(tenantToUserUpdate,tenantToUser)){
			tenantToUserRepository.merge(tenantToUser);
		}
		return tenantToUser;
	}




	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return tenantToUserRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantToUser> getAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext) {
		List<TenantToUser> list= listAllTenantToUsers(tenantToUserFilter, securityContext);
		long count=tenantToUserRepository.countAllTenantToUsers(tenantToUserFilter,securityContext);
		return new PaginationResponse<>(list,tenantToUserFilter,count);
	}

	public List<TenantToUser> listAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext) {
		return tenantToUserRepository.listAllTenantToUsers(tenantToUserFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tenantToUserRepository.findByIds(c, requested);
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

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tenantToUserRepository.findByIdOrNull(type, id);
	}
}