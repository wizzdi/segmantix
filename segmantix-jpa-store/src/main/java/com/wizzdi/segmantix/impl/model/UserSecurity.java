/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;




import com.wizzdi.segmantix.api.model.IUserSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity

public class UserSecurity extends Security implements IUserSecurity {

	@ManyToOne(targetEntity = User.class)
	private User user;

	@ManyToOne(targetEntity = User.class)
	public User getUser() {
		return user;
	}

	public <T extends UserSecurity> T setUser(User user) {
		this.user = user;
		return (T) this;
	}

}
