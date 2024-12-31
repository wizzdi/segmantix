/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.store.jpa.model;

import com.wizzdi.segmantix.api.model.IRoleSecurityLink;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@Entity
public class RoleToBaseclass extends SecurityLink implements IRoleSecurityLink {


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

    @Transient
    @Override
    public SecurityEntity getSecurityEntity() {
        return role;
    }
}
