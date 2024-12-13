/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.api.model.ISecurity;
import jakarta.persistence.*;


@Entity
public class Security implements ISecurity {

	@Id
	private String id;

	private String securedId;

	@ManyToOne(targetEntity = InstanceGroup.class)
	private InstanceGroup instanceGroup;
	private String securedType;

	@ManyToOne(targetEntity = Operation.class)
	private Operation operation;

	@ManyToOne(targetEntity = OperationGroup.class)
	private OperationGroup operationGroup;
	@ManyToOne(targetEntity = SecurityGroup.class)
	private SecurityGroup securityGroup;

	@Enumerated(EnumType.STRING)
	private Access access;



	@ManyToOne(targetEntity = Operation.class)
	public Operation getOperation() {
		return operation;
	}

	public <T extends Security> T setOperation(Operation operation) {
		this.operation = operation;
		return (T) this;
	}

	@Enumerated(EnumType.STRING)
	public Access getAccess() {
		return access;
	}

	public <T extends Security> T setAccess(Access access) {
		this.access = access;
		return (T) this;
	}

	@ManyToOne(targetEntity = InstanceGroup.class)
	public InstanceGroup getInstanceGroup() {
		return instanceGroup;
	}

	public <T extends Security> T setInstanceGroup(InstanceGroup instanceGroup) {
		this.instanceGroup = instanceGroup;
		return (T) this;
	}

	public String getSecuredId() {
		return securedId;
	}

	public <T extends Security> T setSecuredId(String securedId) {
		this.securedId = securedId;
		return (T) this;
	}

	public String getSecuredType() {
		return securedType;
	}

	public <T extends Security> T setSecuredType(String type) {
		this.securedType = type;
		return (T) this;
	}

	@ManyToOne(targetEntity = OperationGroup.class)
	public OperationGroup getOperationGroup() {
		return operationGroup;
	}

	public <T extends Security> T setOperationGroup(OperationGroup operationGroup) {
		this.operationGroup = operationGroup;
		return (T) this;
	}

	@ManyToOne(targetEntity = SecurityGroup.class)
	public SecurityGroup getSecurityGroup() {
		return securityGroup;
	}

	public <T extends Security> T setSecurityGroup(SecurityGroup securityGroup) {
		this.securityGroup = securityGroup;
		return (T) this;
	}

	@Id
	public String getId() {
		return id;
	}

	public <T extends Security> T setId(String id) {
		this.id = id;
		return (T) this;
	}
}
