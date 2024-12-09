/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;




import com.wizzdi.segmantix.api.ITenantToUser;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;





@Entity
public class TenantToUser extends Basic implements ITenantToUser {

    private boolean defaultTenant;
    @ManyToOne(targetEntity = SecurityTenant.class)
    private SecurityTenant tenant;
    @ManyToOne(targetEntity = SecurityUser.class)
    private SecurityUser user;

    public boolean isDefaultTenant() {
        return defaultTenant;
    }

    public <T extends TenantToUser> T setDefaultTenant(boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
        return (T) this;
    }

    @ManyToOne(targetEntity = SecurityTenant.class)
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToUser> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    @ManyToOne(targetEntity = SecurityUser.class)
    public SecurityUser getUser() {
        return user;
    }

    public <T extends TenantToUser> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }
}
