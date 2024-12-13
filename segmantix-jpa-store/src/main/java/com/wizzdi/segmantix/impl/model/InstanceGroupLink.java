/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;



import com.wizzdi.segmantix.api.model.IInstanceGroupLink;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;




@Entity
public class InstanceGroupLink extends Basic implements IInstanceGroupLink {


	@ManyToOne(targetEntity = InstanceGroup.class)
	private InstanceGroup instanceGroup;
	private String securedId;
	private String securedType;

	@ManyToOne(targetEntity = InstanceGroup.class)

	public InstanceGroup getInstanceGroup() {
		return instanceGroup;
	}

	public <T extends InstanceGroupLink> T setInstanceGroup(InstanceGroup instanceGroup) {
		this.instanceGroup = instanceGroup;
		return (T) this;
	}

	public String getSecuredId() {
		return securedId;
	}

	public <T extends InstanceGroupLink> T setSecuredId(String securedId) {
		this.securedId = securedId;
		return (T) this;
	}

	public String getSecuredType() {
		return securedType;
	}

	public <T extends InstanceGroupLink> T setSecuredType(String securedType) {
		this.securedType = securedType;
		return (T) this;
	}
}
