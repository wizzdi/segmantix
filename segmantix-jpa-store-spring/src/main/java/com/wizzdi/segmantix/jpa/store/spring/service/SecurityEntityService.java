package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.SecurityEntity;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityEntityCreate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class SecurityEntityService  {


	@Autowired
	private BasicService basicService;


	public boolean updateNoMerge(SecurityEntityCreate securityEntityCreate, SecurityEntity securityEntity){
		return basicService.updateBasicNoMerge(securityEntityCreate,securityEntity);
	}

}
