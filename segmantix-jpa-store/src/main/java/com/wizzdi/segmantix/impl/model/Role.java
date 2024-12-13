/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;





import com.wizzdi.segmantix.api.model.IRole;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Entity implementation class for Entity: Role
 * Note that the default roles are defines through annotation and all operations are linked to these roles while operations are created.
 *
 */


@Entity
public class Role extends SecurityEntity implements IRole {


	
	@OneToMany(mappedBy="role",targetEntity=RoleToUser.class) //users are subscribed to very few roles.
	private List<RoleToUser> roleToUser =new ArrayList<>();


	@OneToMany(targetEntity = RoleSecurity.class,mappedBy="role", fetch=FetchType.LAZY)
	
	private List<RoleSecurity> roleSecurity =new ArrayList<>();


	
	@OneToMany(mappedBy="role", targetEntity=RoleToUser.class) //users are subscribed to very few roles.
	public List<RoleToUser> getRoleToUser() {
		return roleToUser;
	}

	public void setRoleToUser(List<RoleToUser> users) {
		this.roleToUser = users;
	}



	@OneToMany(targetEntity = RoleSecurity.class,mappedBy="role", fetch=FetchType.LAZY)
	
	public List<RoleSecurity> getRoleSecurity() {
		return roleSecurity;
	}

	public void setRoleSecurity(List<RoleSecurity> baseclasses) {
		this.roleSecurity = baseclasses;
	}

	@Transient
	public Tenant getTenant(){
		return Optional.ofNullable(getTenant()).orElse(null);
	}

}
