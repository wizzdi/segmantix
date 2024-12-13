/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.api.model.IOperation;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Operation
 * Default Operations are created differently from other classes, methods are automatically extracted from all classes annotated with OperationsInside and IOperation on the method itself
 */


@Entity

public class Operation extends Basic implements IOperation {

	private Access defaultAccess;

	private String category;

	
	@OneToMany(targetEntity = OperationGroupLink.class,mappedBy = "operation")
	private List<OperationGroupLink> operationGroupLinks=new ArrayList<>();

	public Access getDefaultAccess() {
		return defaultAccess;
	}

	public <T extends Operation> T setDefaultAccess(Access defaultaccess) {
		this.defaultAccess = defaultaccess;
		return (T) this;
	}

	public String getCategory() {
		return category;
	}

	public <T extends Operation> T setCategory(String category) {
		this.category = category;
		return (T) this;
	}

	
	@OneToMany(targetEntity = OperationGroupLink.class,mappedBy = "operation")
	public List<OperationGroupLink> getOperationGroupLinks() {
		return operationGroupLinks;
	}

	public <T extends Operation> T setOperationGroupLinks(List<OperationGroupLink> operationGroupLinks) {
		this.operationGroupLinks = operationGroupLinks;
		return (T) this;
	}
}
