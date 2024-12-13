/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;




import com.wizzdi.segmantix.api.model.IInstanceGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
public class InstanceGroup extends Baseclass implements IInstanceGroup {



	private String externalId;



	@OneToMany(targetEntity = InstanceGroupLink.class,mappedBy="instanceGroup")
	
	private List<InstanceGroupLink> links =new ArrayList<>();

	@OneToMany(targetEntity = InstanceGroupLink.class,mappedBy="instanceGroup")
	
	public List<InstanceGroupLink> getLinks() {
		return links;
	}

	public InstanceGroup setLinks(List<InstanceGroupLink> instanceGroupLinkes) {
		this.links = instanceGroupLinkes;
		return this;
	}

	public String getExternalId() {
		return externalId;
	}

	public <T extends InstanceGroup> T setExternalId(String externalId) {
		this.externalId = externalId;
		return (T) this;
	}
}
