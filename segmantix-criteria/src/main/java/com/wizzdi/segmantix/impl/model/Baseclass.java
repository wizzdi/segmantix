/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;

import com.wizzdi.segmantix.api.Secured;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public class Baseclass extends Basic implements Secured {

	@ManyToOne(targetEntity = SecurityUser.class)
	private  SecurityUser creator;
	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant tenant;




	public SecurityUser getCreator() {
		return creator;
	}

	public <T extends Baseclass> T setCreator(SecurityUser creator) {
		this.creator = creator;
		return (T) this;
	}

	public SecurityTenant getTenant() {
		return tenant;
	}

	public <T extends Baseclass> T setTenant(SecurityTenant tenant) {
		this.tenant = tenant;
		return (T) this;
	}

	@Override
	public String getCreatorId() {
		return getCreator().getId();
	}

	@Override
	public String getTenantId() {
		return getTenant().getId();
	}
}
