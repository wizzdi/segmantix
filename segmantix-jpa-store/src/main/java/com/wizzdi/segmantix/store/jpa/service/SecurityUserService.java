package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.SecurityUserRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;
import com.wizzdi.segmantix.store.jpa.request.SecurityUserCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityUserFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityUserUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class SecurityUserService implements SegmantixService {

	
	private final SecurityEntityService securityEntityService;
	private final SecurityUserRepository securityUserRepository;

	public SecurityUserService(SecurityEntityService securityEntityService, SecurityUserRepository securityUserRepository) {
		this.securityEntityService = securityEntityService;
		this.securityUserRepository = securityUserRepository;
	}

	public SecurityUser createSecurityUser(SecurityUserCreate securityUserCreate, SecurityContext securityContext){
		SecurityUser securityUser= createSecurityUserNoMerge(securityUserCreate,securityContext);
		securityUserRepository.merge(securityUser);
		return securityUser;
	}


	public SecurityUser createSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityContext securityContext){
		SecurityUser securityUser=new SecurityUser();
		securityUser.setId(UUID.randomUUID().toString());
		updateSecurityUserNoMerge(securityUserCreate,securityUser);
		BaseclassService.createSecurityObjectNoMerge(securityUser,securityContext);
		return securityUser;
	}

	public boolean updateSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityUser securityUser) {
		return securityEntityService.updateNoMerge(securityUserCreate,securityUser);
	}

	public SecurityUser updateSecurityUser(SecurityUserUpdate securityUserUpdate, SecurityContext securityContext){
		SecurityUser securityUser=securityUserUpdate.getSecurityUser();
		if(updateSecurityUserNoMerge(securityUserUpdate,securityUser)){
			securityUserRepository.merge(securityUser);
		}
		return securityUser;
	}



	public PaginationResponse<SecurityUser> getAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		List<SecurityUser> list= listAllSecurityUsers(securityUserFilter, securityContext);
		long count=securityUserRepository.countAllSecurityUsers(securityUserFilter,securityContext);
		return new PaginationResponse<>(list,securityUserFilter,count);
	}

	public List<SecurityUser> listAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		return securityUserRepository.listAllSecurityUsers(securityUserFilter, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityUserRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityUserRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securityUserRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securityUserRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securityUserRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securityUserRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securityUserRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return securityUserRepository.merge(base);
	}



	public <T> T merge(T base, boolean updateDate) {
		return securityUserRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		securityUserRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		securityUserRepository.massMerge(toMerge, updatedate);
	}
}
