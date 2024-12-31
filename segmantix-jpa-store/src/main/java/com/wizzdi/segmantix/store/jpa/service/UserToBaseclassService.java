package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.UserToBaseclassRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass;
import com.wizzdi.segmantix.store.jpa.request.UserToBaseclassCreate;
import com.wizzdi.segmantix.store.jpa.request.UserToBaseclassFilter;
import com.wizzdi.segmantix.store.jpa.request.UserToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class UserToBaseclassService  implements SegmantixService {

	
	private final SecurityLinkService securityLinkService;
	private final UserToBaseclassRepository userToBaseclassRepository;

	public UserToBaseclassService(SecurityLinkService securityLinkService, UserToBaseclassRepository userToBaseclassRepository) {
		this.securityLinkService = securityLinkService;
		this.userToBaseclassRepository = userToBaseclassRepository;
	}

	public UserToBaseclass createUserToBaseclass(UserToBaseclassCreate userToBaseclassCreate, SecurityContext securityContext){
		UserToBaseclass userToBaseclass= createUserToBaseclassNoMerge(userToBaseclassCreate,securityContext);
		userToBaseclassRepository.merge(userToBaseclass);
		return userToBaseclass;
	}


	public UserToBaseclass createUserToBaseclassNoMerge(UserToBaseclassCreate userToBaseclassCreate, SecurityContext securityContext){
		UserToBaseclass userToBaseclass=new UserToBaseclass();
		userToBaseclass.setId(UUID.randomUUID().toString());
		updateUserToBaseclassNoMerge(userToBaseclassCreate,userToBaseclass);
		BaseclassService.createSecurityObjectNoMerge(userToBaseclass,securityContext);
		return userToBaseclass;
	}

	public boolean updateUserToBaseclassNoMerge(UserToBaseclassCreate userToBaseclassCreate, UserToBaseclass userToBaseclass) {
		boolean update = securityLinkService.updateSecurityLinkNoMerge(userToBaseclassCreate, userToBaseclass);
		if(userToBaseclassCreate.getUser()!=null&&(userToBaseclass.getUser()==null||!userToBaseclassCreate.getUser().getId().equals(userToBaseclass.getUser().getId()))){
			userToBaseclass.setUser(userToBaseclassCreate.getUser());
			update=true;
		}
		return update;
	}

	public UserToBaseclass updateUserToBaseclass(UserToBaseclassUpdate userToBaseclassUpdate, SecurityContext securityContext){
		UserToBaseclass userToBaseclass=userToBaseclassUpdate.getUserToBaseclass();
		if(updateUserToBaseclassNoMerge(userToBaseclassUpdate,userToBaseclass)){
			userToBaseclassRepository.merge(userToBaseclass);
		}
		return userToBaseclass;
	}
	public void merge(Object o){
		userToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		userToBaseclassRepository.massMerge(list);
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return userToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return userToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public PaginationResponse<UserToBaseclass> getAllUserToBaseclass(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext) {
		List<UserToBaseclass> list= listAllUserToBaseclasss(userToBaseclassFilter, securityContext);
		long count=userToBaseclassRepository.countAllUserToBaseclasss(userToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,userToBaseclassFilter,count);
	}

	public List<UserToBaseclass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext) {
		return userToBaseclassRepository.listAllUserToBaseclasss(userToBaseclassFilter, securityContext);
	}
}
