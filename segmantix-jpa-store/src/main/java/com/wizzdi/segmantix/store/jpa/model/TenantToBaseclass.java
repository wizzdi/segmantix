/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.store.jpa.model;



import com.wizzdi.segmantix.api.model.ITenantSecurityLink;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@Entity

public class TenantToBaseclass extends SecurityLink implements ITenantSecurityLink {


	@Override
	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTenant() {
		return super.getTenant();
	}

	@Override
	public void setTenant(SecurityTenant tenant) {
		super.setTenant(tenant);
	}

	@Transient
	@Override
	public SecurityEntity getSecurityEntity() {
		return getTenant();
	}
}
