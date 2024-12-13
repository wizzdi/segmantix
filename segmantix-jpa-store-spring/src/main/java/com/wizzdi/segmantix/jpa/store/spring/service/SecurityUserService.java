package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.User;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.UserRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.UserCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.UserFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.UserUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class UserService  {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private UserRepository userRepository;


	public User createUser(UserCreate userCreate, SecurityContext securityContext){
		User user= createUserNoMerge(userCreate,securityContext);
		userRepository.merge(user);
		return user;
	}


	public User createUserNoMerge(UserCreate userCreate, SecurityContext securityContext){
		User user=new User();
		user.setId(UUID.randomUUID().toString());
		updateUserNoMerge(userCreate,user);
		BaseclassService.createSecurityObjectNoMerge(user,securityContext);
		return user;
	}

	public boolean updateUserNoMerge(UserCreate userCreate, User user) {
		return securityEntityService.updateNoMerge(userCreate,user);
	}

	public User updateUser(UserUpdate userUpdate, SecurityContext securityContext){
		User user=userUpdate.getUser();
		if(updateUserNoMerge(userUpdate,user)){
			userRepository.merge(user);
		}
		return user;
	}



	public PaginationResponse<User> getAllUsers(UserFilter userFilter, SecurityContext securityContext) {
		List<User> list= listAllUsers(userFilter, securityContext);
		long count=userRepository.countAllUsers(userFilter,securityContext);
		return new PaginationResponse<>(list,userFilter,count);
	}

	public List<User> listAllUsers(UserFilter userFilter, SecurityContext securityContext) {
		return userRepository.listAllUsers(userFilter, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return userRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return userRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return userRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return userRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return userRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return userRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return userRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return userRepository.merge(base);
	}


	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return userRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		userRepository.massMerge(toMerge, updatedate, propagateEvents);
	}


	public <T> T merge(T base, boolean updateDate) {
		return userRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		userRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		userRepository.massMerge(toMerge, updatedate);
	}
}
