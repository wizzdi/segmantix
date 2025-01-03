/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.store.jpa.model;

import com.wizzdi.segmantix.api.model.IUser;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

//the table name 'user' isn't allowed in Postgresql

@Table(name = "UserTable")
@Entity

public class SecurityUser extends SecurityEntity implements IUser {

	@OneToMany(targetEntity = RoleToUser.class,mappedBy = "user")
	private List<RoleToUser> roles=new ArrayList<>();
	@OneToMany(targetEntity = TenantToUser.class,mappedBy = "user")
	private List<TenantToUser> tenants=new ArrayList<>();

	@OneToMany(targetEntity = UserToBaseclass.class,mappedBy = "user")
	private List<UserToBaseclass> userToBaseclasses =new ArrayList<>();

	@OneToMany(targetEntity = RoleToUser.class,mappedBy = "user")
	public List<RoleToUser> getRoles() {
		return roles;
	}

	public <T extends SecurityUser> T setRoles(List<RoleToUser> roles) {
		this.roles = roles;
		return (T) this;
	}

	@OneToMany(targetEntity = UserToBaseclass.class,mappedBy = "user")
	public List<UserToBaseclass> getUserToBaseClasses() {
		return userToBaseclasses;
	}

	public <T extends SecurityUser> T setUserToBaseClasses(List<UserToBaseclass> userToBaseclasses) {
		this.userToBaseclasses = userToBaseclasses;
		return (T) this;
	}

	@OneToMany(targetEntity = TenantToUser.class,mappedBy = "user")
	public List<TenantToUser> getTenants() {
		return tenants;
	}

	public <T extends SecurityUser> T setTenants(List<TenantToUser> tenants) {
		this.tenants = tenants;
		return (T) this;
	}
}
