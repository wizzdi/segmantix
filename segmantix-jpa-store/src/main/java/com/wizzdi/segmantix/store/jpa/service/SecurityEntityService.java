package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.SecurityEntity;
import com.wizzdi.segmantix.store.jpa.request.SecurityEntityCreate;


public class SecurityEntityService implements SegmantixService {


	private final BasicService basicService;

    public SecurityEntityService(BasicService basicService) {
        this.basicService = basicService;
    }


    public boolean updateNoMerge(SecurityEntityCreate securityEntityCreate, SecurityEntity securityEntity){
		return basicService.updateBasicNoMerge(securityEntityCreate,securityEntity);
	}

}
