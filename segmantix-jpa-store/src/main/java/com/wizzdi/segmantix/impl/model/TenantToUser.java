/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;




import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;





@Entity
public class TenantToUser extends Basic  {

    private boolean defaultTenant;
    @ManyToOne(targetEntity = Tenant.class)
    private Tenant tenant;
    @ManyToOne(targetEntity = User.class)
    private User user;

    public boolean isDefaultTenant() {
        return defaultTenant;
    }

    public <T extends TenantToUser> T setDefaultTenant(boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
        return (T) this;
    }

    @ManyToOne(targetEntity = Tenant.class)
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends TenantToUser> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    @ManyToOne(targetEntity = User.class)
    public User getUser() {
        return user;
    }

    public <T extends TenantToUser> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
