package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.UserSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.UserSecurityRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class UserSecurityService  {

	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserSecurityRepository userSecurityRepository;


	public UserSecurity createUserSecurity(UserSecurityCreate userSecurityCreate, SecurityContext securityContext){
		UserSecurity userSecurity= createUserSecurityNoMerge(userSecurityCreate,securityContext);
		userSecurityRepository.merge(userSecurity);
		return userSecurity;
	}


	public UserSecurity createUserSecurityNoMerge(UserSecurityCreate userSecurityCreate, SecurityContext securityContext){
		UserSecurity userSecurity=new UserSecurity();
		userSecurity.setId(UUID.randomUUID().toString());
		updateUserSecurityNoMerge(userSecurityCreate,userSecurity);
		BaseclassService.createSecurityObjectNoMerge(userSecurity,securityContext);
		return userSecurity;
	}

	public boolean updateUserSecurityNoMerge(UserSecurityCreate userSecurityCreate, UserSecurity userSecurity) {
		boolean update = securityService.updateSecurityNoMerge(userSecurityCreate, userSecurity);
		if(userSecurityCreate.getUser()!=null&&(userSecurity.getUser()==null||!userSecurityCreate.getUser().getId().equals(userSecurity.getUser().getId()))){
			userSecurity.setUser(userSecurityCreate.getUser());
			update=true;
		}
		return update;
	}

	public UserSecurity updateUserSecurity(UserSecurityUpdate userSecurityUpdate, SecurityContext securityContext){
		UserSecurity userSecurity=userSecurityUpdate.getUserSecurity();
		if(updateUserSecurityNoMerge(userSecurityUpdate,userSecurity)){
			userSecurityRepository.merge(userSecurity);
		}
		return userSecurity;
	}
	public void merge(Object o){
		userSecurityRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		userSecurityRepository.massMerge(list);
	}



	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return userSecurityRepository.getByIdOrNull(id,c,securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return userSecurityRepository.listByIds(c, ids, securityContext);
	}

	public PaginationResponse<UserSecurity> getAllUserSecurity(UserSecurityFilter userSecurityFilter, SecurityContext securityContext) {
		List<UserSecurity> list= listAllUserSecuritys(userSecurityFilter, securityContext);
		long count=userSecurityRepository.countAllUserSecuritys(userSecurityFilter,securityContext);
		return new PaginationResponse<>(list,userSecurityFilter,count);
	}

	public List<UserSecurity> listAllUserSecuritys(UserSecurityFilter userSecurityFilter, SecurityContext securityContext) {
		return userSecurityRepository.listAllUserSecuritys(userSecurityFilter, securityContext);
	}
}
