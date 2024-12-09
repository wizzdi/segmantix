/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.api.Access;
import com.wizzdi.segmantix.api.ISecurityOperation;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Operation
 * Default Operations are created differently from other classes, methods are automatically extracted from all classes annotated with OperationsInside and IOperation on the method itself
 */


@Entity

public class SecurityOperation extends Basic implements ISecurityOperation {

	private Access defaultAccess;

	private String category;

	
	@OneToMany(targetEntity = OperationToGroup.class,mappedBy = "operation")
	private List<OperationToGroup> operationToGroups=new ArrayList<>();

	public Access getDefaultAccess() {
		return defaultAccess;
	}

	public <T extends SecurityOperation> T setDefaultAccess(Access defaultaccess) {
		this.defaultAccess = defaultaccess;
		return (T) this;
	}

	public String getCategory() {
		return category;
	}

	public <T extends SecurityOperation> T setCategory(String category) {
		this.category = category;
		return (T) this;
	}

	
	@OneToMany(targetEntity = OperationToGroup.class,mappedBy = "operation")
	public List<OperationToGroup> getOperationToGroups() {
		return operationToGroups;
	}

	public <T extends SecurityOperation> T setOperationToGroups(List<OperationToGroup> operationToGroups) {
		this.operationToGroups = operationToGroups;
		return (T) this;
	}
}
