package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecuredBasic;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.BaseclassRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.BaseclassCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.BaseclassFilter;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class BaseclassService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private BaseclassRepository baseclassRepository;


	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = basicService.updateBasicNoMerge(baseclassCreate,baseclass);

		if (baseclassCreate.getSystemObject() != null && (!baseclassCreate.getSystemObject().equals(baseclass.isSystemObject()))) {
			baseclass.setSystemObject(baseclassCreate.getSystemObject());
			update = true;
		}

		return update;
	}

	public PaginationResponse<Baseclass> getAllBaseclass(BaseclassFilter baseclasssFilter , SecurityContext securityContext) {
		List<Baseclass> baseclasses = listAllBaseclass(baseclasssFilter, securityContext);
		long count = baseclassRepository.countAllBaseclass(baseclasssFilter,securityContext);
		return new PaginationResponse<>(baseclasses, baseclasssFilter, count);
	}

	public List<Baseclass> listAllBaseclass(BaseclassFilter baseclasssFilter , SecurityContext securityContext) {
		return baseclassRepository.listAllBaseclass(baseclasssFilter,securityContext);
	}

	@Deprecated
	public void validate(BaseclassCreate baseclassCreate, SecurityContext securityContext) {
		basicService.validate(baseclassCreate,securityContext);
	}


	public static <T extends SecuredBasic> Baseclass createSecurityObjectNoMerge(T subject, SecurityContext securityContext) {
		Baseclass security=new Baseclass(subject.getName(),subject.getClass(),securityContext);
		subject.setSecurity(security);
		return security;
	}


}
