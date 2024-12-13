/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;



import com.wizzdi.segmantix.api.model.ITenantSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity

public class TenantSecurity extends Security implements ITenantSecurity {

	@ManyToOne(targetEntity = Tenant.class)
	private Tenant tenant;

	@ManyToOne(targetEntity = Tenant.class)
	public Tenant getTenant() {
		return tenant;
	}

	public <T extends TenantSecurity> T setTenant(Tenant tenant) {
		this.tenant = tenant;
		return (T) this;
	}

}
