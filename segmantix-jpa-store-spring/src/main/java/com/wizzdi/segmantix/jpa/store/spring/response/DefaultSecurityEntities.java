package com.wizzdi.segmantix.jpa.store.spring.response;


import com.wizzdi.segmantix.impl.model.Role;
import com.wizzdi.segmantix.impl.model.RoleToUser;
import com.wizzdi.segmantix.impl.model.Tenant;
import com.wizzdi.segmantix.impl.model.TenantToUser;
import com.wizzdi.segmantix.impl.model.User;

public class DefaultSecurityEntities {

	private final User user;
	private final Tenant tenant;
	private final Role role;
	private final TenantToUser tenantToUser;
	private final RoleToUser roleToUser;


	public DefaultSecurityEntities(User user, Tenant tenant, Role role, TenantToUser tenantToUser, RoleToUser roleToUser) {
		this.user = user;
		this.tenant = tenant;
		this.role = role;
		this.tenantToUser = tenantToUser;
		this.roleToUser = roleToUser;
	}

	public User getUser() {
		return user;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public Role getRole() {
		return role;
	}

	public TenantToUser getTenantToUser() {
		return tenantToUser;
	}

	public RoleToUser getRoleToUser() {
		return roleToUser;
	}
}
