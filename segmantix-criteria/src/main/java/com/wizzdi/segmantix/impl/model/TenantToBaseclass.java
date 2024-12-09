/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;



import com.wizzdi.segmantix.api.ITenantToBaseclass;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity

public class TenantToBaseclass extends SecurityLink implements ITenantToBaseclass {

	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant tenant;

	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTenant() {
		return tenant;
	}

	public <T extends TenantToBaseclass> T setTenant(SecurityTenant securityTenant) {
		this.tenant = securityTenant;
		return (T) this;
	}

}
