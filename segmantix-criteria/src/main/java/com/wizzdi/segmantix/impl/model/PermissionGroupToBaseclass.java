/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;



import com.wizzdi.segmantix.api.IPermissionGroupToBaseclass;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;




@Entity
public class PermissionGroupToBaseclass extends Basic implements IPermissionGroupToBaseclass {


	@ManyToOne(targetEntity = PermissionGroup.class)
	private PermissionGroup permissionGroup;
	private String securedId;
	private String securedType;

	@ManyToOne(targetEntity = PermissionGroup.class)

	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public <T extends PermissionGroupToBaseclass> T setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return (T) this;
	}

	public String getSecuredId() {
		return securedId;
	}

	public <T extends PermissionGroupToBaseclass> T setSecuredId(String securedId) {
		this.securedId = securedId;
		return (T) this;
	}

	public String getSecuredType() {
		return securedType;
	}

	public <T extends PermissionGroupToBaseclass> T setSecuredType(String securedType) {
		this.securedType = securedType;
		return (T) this;
	}
}
