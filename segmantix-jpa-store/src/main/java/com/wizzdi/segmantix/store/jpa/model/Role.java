/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.store.jpa.model;

import com.wizzdi.segmantix.api.model.IRole;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Role
 * Note that the default roles are defines through annotation and all operations are linked to these roles while operations are created.
 *
 */

@Entity
public class Role extends SecurityEntity implements IRole {


	@OneToMany(mappedBy="role",targetEntity=RoleToUser.class) //users are subscribed to very few roles.
	private List<RoleToUser> roleToUser =new ArrayList<>();
	private boolean superAdmin;


	@OneToMany(targetEntity = RoleToBaseclass.class,mappedBy="role", fetch=FetchType.LAZY)
	private List<RoleToBaseclass> roleToBaseclass =new ArrayList<>();


	@OneToMany(mappedBy="role", targetEntity=RoleToUser.class) //users are subscribed to very few roles.
	public List<RoleToUser> getRoleToUser() {
		return roleToUser;
	}

	public void setRoleToUser(List<RoleToUser> users) {
		this.roleToUser = users;
	}



	@OneToMany(targetEntity = RoleToBaseclass.class,mappedBy="role", fetch=FetchType.LAZY)
	public List<RoleToBaseclass> getRoleToBaseclass() {
		return roleToBaseclass;
	}

	public void setRoleToBaseclass(List<RoleToBaseclass> baseclasses) {
		this.roleToBaseclass = baseclasses;
	}


	@Override
	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTenant() {
		return super.getTenant();
	}

	@Override
	public void setTenant(SecurityTenant tenant) {
		super.setTenant(tenant);
	}

	@Override
	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public <T extends Role> T setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
		return (T) this;
	}
}
