package com.wizzdi.segmantix.store.jpa.model;

import com.wizzdi.segmantix.api.model.ISecurityGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SecurityLinkGroup extends Baseclass implements ISecurityGroup {

    @OneToMany(targetEntity = SecurityLink.class, mappedBy = "securityLinkGroup")
    private List<SecurityLink> securityLinks = new ArrayList<>();

    @OneToMany(targetEntity = SecurityLink.class, mappedBy = "securityLinkGroup")
    public List<SecurityLink> getSecurityLinks() {
        return securityLinks;
    }

    public <T extends SecurityLinkGroup> T setSecurityLinks(List<SecurityLink> securityLinks) {
        this.securityLinks = securityLinks;
        return (T) this;
    }
}
