package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.api.model.ISecurityGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SecurityGroup extends Baseclass implements ISecurityGroup {

    
    @OneToMany(targetEntity = Security.class, mappedBy = "securityGroup")
    private List<Security> securitys = new ArrayList<>();

    
    @OneToMany(targetEntity = Security.class, mappedBy = "securityGroup")
    public List<Security> getSecuritys() {
        return securitys;
    }

    public <T extends SecurityGroup> T setSecuritys(List<Security> securitys) {
        this.securitys = securitys;
        return (T) this;
    }
}
