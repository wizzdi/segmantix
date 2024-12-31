package com.wizzdi.segmantix.store.jpa.request;


import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;
public class RoleCreate extends SecurityEntityCreate {


    private SecurityTenant tenant;
    
    private Boolean superAdmin;

    public RoleCreate(RoleCreate other) {
        super(other);
        this.tenant=other.tenant;
        this.superAdmin=other.superAdmin;
    }

    public RoleCreate() {
    }


    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends RoleCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    
    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public <T extends RoleCreate> T setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
        return (T) this;
    }
}
