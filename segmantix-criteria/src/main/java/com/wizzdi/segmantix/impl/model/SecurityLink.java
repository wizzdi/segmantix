/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.segmantix.impl.model;


import com.wizzdi.segmantix.api.Access;
import com.wizzdi.segmantix.api.ISecurityLink;
import jakarta.persistence.*;


@Entity
public class SecurityLink  implements ISecurityLink {

	@Id
	private String id;

	private String securedId;

	@ManyToOne(targetEntity = PermissionGroup.class)
	private PermissionGroup permissionGroup;
	private String securedType;

	@ManyToOne(targetEntity = SecurityOperation.class)
	private SecurityOperation operation;

	@ManyToOne(targetEntity = OperationGroup.class)
	private OperationGroup operationGroup;
	@ManyToOne(targetEntity = SecurityLinkGroup.class)
	private SecurityLinkGroup securityLinkGroup;

	@Enumerated(EnumType.STRING)
	private com.wizzdi.segmantix.api.Access access;



	@ManyToOne(targetEntity = SecurityOperation.class)
	public SecurityOperation getOperation() {
		return operation;
	}

	public <T extends SecurityLink> T setOperation(SecurityOperation operation) {
		this.operation = operation;
		return (T) this;
	}

	@Enumerated(EnumType.STRING)
	public Access getAccess() {
		return access;
	}

	public <T extends SecurityLink> T setAccess(com.wizzdi.segmantix.api.Access access) {
		this.access = access;
		return (T) this;
	}

	@ManyToOne(targetEntity = PermissionGroup.class)
	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public <T extends SecurityLink> T setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return (T) this;
	}

	public String getSecuredId() {
		return securedId;
	}

	public <T extends SecurityLink> T setSecuredId(String securedId) {
		this.securedId = securedId;
		return (T) this;
	}

	public String getSecuredType() {
		return securedType;
	}

	public <T extends SecurityLink> T setSecuredType(String type) {
		this.securedType = type;
		return (T) this;
	}

	@ManyToOne(targetEntity = OperationGroup.class)
	public OperationGroup getOperationGroup() {
		return operationGroup;
	}

	public <T extends SecurityLink> T setOperationGroup(OperationGroup operationGroup) {
		this.operationGroup = operationGroup;
		return (T) this;
	}

	@ManyToOne(targetEntity = SecurityLinkGroup.class)
	public SecurityLinkGroup getSecurityLinkGroup() {
		return securityLinkGroup;
	}

	public <T extends SecurityLink> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
		this.securityLinkGroup = securityLinkGroup;
		return (T) this;
	}

	@Id
	public String getId() {
		return id;
	}

	public <T extends SecurityLink> T setId(String id) {
		this.id = id;
		return (T) this;
	}
}
