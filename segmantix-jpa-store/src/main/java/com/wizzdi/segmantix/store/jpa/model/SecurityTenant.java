/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.store.jpa.model;

import com.wizzdi.segmantix.api.model.ITenant;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Operation
 *
 */

@Entity
public class SecurityTenant extends SecurityEntity implements ITenant {

	private String externalId;



	@OneToMany(targetEntity = TenantToBaseclass.class,mappedBy="tenant")
	private List<TenantToBaseclass> tenantToBaseclasses =new ArrayList<>();

	@OneToMany(targetEntity = TenantToUser.class,mappedBy= "tenant")
	private List<TenantToUser> tenantToUser=new ArrayList<>();

	@OneToMany(targetEntity = TenantToUser.class,mappedBy= "tenant")
	public List<TenantToUser> getTenantToUser() {
		return tenantToUser;
	}

	public void setTenantToUser(List<TenantToUser> tenantToUser) {
		this.tenantToUser = tenantToUser;
	}


	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
