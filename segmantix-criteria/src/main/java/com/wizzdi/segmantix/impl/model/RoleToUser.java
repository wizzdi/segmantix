/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.api.IRoleToUser;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: RoleToUser
 *
 */


@Entity

public class RoleToUser extends Basic implements IRoleToUser {

    private Role role;
    private SecurityUser user;

    @ManyToOne(targetEntity = Role.class)
    public Role getRole() {
        return role;
    }

    public <T extends RoleToUser> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    @ManyToOne(targetEntity = SecurityUser.class)
    public SecurityUser getUser() {
        return user;
    }

    public <T extends RoleToUser> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }
}
