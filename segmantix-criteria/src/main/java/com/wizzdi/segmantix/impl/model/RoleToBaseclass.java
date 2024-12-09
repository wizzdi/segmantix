/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.api.IRoleToBaseclass;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity
public class RoleToBaseclass extends SecurityLink implements IRoleToBaseclass {


    @ManyToOne(targetEntity = Role.class)
    private Role role;

    @ManyToOne(targetEntity = Role.class)
    public Role getRole() {
        return role;
    }

    public <T extends RoleToBaseclass> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

}
