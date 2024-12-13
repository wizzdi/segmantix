package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

import java.time.OffsetDateTime;

@IdValid.List({
        @IdValid(targetField = "policyRole", fieldType = Role.class, field = "policyRoleId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "policyTenant", fieldType = Tenant.class, field = "policyTenantId", groups = {Create.class, Update.class})

})
public class SecurityPolicyCreate extends BasicCreate {


    private OffsetDateTime startTime;
    private Boolean enabled;
    private String policyRoleId;
    @JsonIgnore
    private Role policyRole;
    private String policyTenantId;
    @JsonIgnore
    private Tenant policyTenant;


    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public <T extends SecurityPolicyCreate> T setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends SecurityPolicyCreate> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public String getPolicyRoleId() {
        return policyRoleId;
    }

    public <T extends SecurityPolicyCreate> T setPolicyRoleId(String policyRoleId) {
        this.policyRoleId = policyRoleId;
        return (T) this;
    }

    @JsonIgnore
    public Role getPolicyRole() {
        return policyRole;
    }

    public <T extends SecurityPolicyCreate> T setPolicyRole(Role policyRole) {
        this.policyRole = policyRole;
        return (T) this;
    }

    public String getPolicyTenantId() {
        return policyTenantId;
    }

    public <T extends SecurityPolicyCreate> T setPolicyTenantId(String policyTenantId) {
        this.policyTenantId = policyTenantId;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getPolicyTenant() {
        return policyTenant;
    }

    public <T extends SecurityPolicyCreate> T setPolicyTenant(Tenant policyTenant) {
        this.policyTenant = policyTenant;
        return (T) this;
    }


}
