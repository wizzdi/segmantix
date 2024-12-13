package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.security.SecurityPolicy;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.SecurityPolicyRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class SecurityPolicyService  {

	@Autowired
	private SecurityPolicyRepository securityPolicyRepository;

	@Autowired
	private BasicService basicService;


	public SecurityPolicy createSecurityPolicy(SecurityPolicyCreate securityPolicyCreate, SecurityContext securityContext) {
		SecurityPolicy securityPolicy = createSecurityPolicyNoMerge(securityPolicyCreate, securityContext);
		securityPolicyRepository.merge(securityPolicy);
		return securityPolicy;
	}

	public void merge(Object o) {
		securityPolicyRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		securityPolicyRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityPolicyRepository.listByIds(c, ids, securityContext);
	}

	public SecurityPolicy createSecurityPolicyNoMerge(SecurityPolicyCreate securityPolicyCreate, SecurityContext securityContext) {
		SecurityPolicy securityPolicy = new SecurityPolicy();
		securityPolicy.setId(UUID.randomUUID().toString());
		updateSecurityPolicyNoMerge(securityPolicyCreate, securityPolicy);
		BaseclassService.createSecurityObjectNoMerge(securityPolicy, securityContext);
		return securityPolicy;
	}

	public boolean updateSecurityPolicyNoMerge(SecurityPolicyCreate securityPolicyCreate, SecurityPolicy securityPolicy) {
		boolean update = basicService.updateBasicNoMerge(securityPolicyCreate,securityPolicy);

		if (securityPolicyCreate.getStartTime() != null && !securityPolicyCreate.getStartTime().equals(securityPolicy.getStartTime())) {
			securityPolicy.setStartTime(securityPolicyCreate.getStartTime());
			update = true;
		}

		if (securityPolicyCreate.getEnabled() != null && !securityPolicyCreate.getEnabled().equals(securityPolicy.isEnabled())) {
			securityPolicy.setEnabled(securityPolicyCreate.getEnabled());
			update = true;
		}

		if (securityPolicyCreate.getPolicyRole() != null && (securityPolicy.getPolicyRole() == null || !securityPolicyCreate.getPolicyRole().getId().equals(securityPolicy.getPolicyRole().getId()))) {
			securityPolicy.setPolicyRole(securityPolicyCreate.getPolicyRole());
			update = true;
		}

		if (securityPolicyCreate.getPolicyTenant() != null && (securityPolicy.getPolicyTenant() == null || !securityPolicyCreate.getPolicyTenant().getId().equals(securityPolicy.getPolicyTenant().getId()))) {
			securityPolicy.setPolicyTenant(securityPolicyCreate.getPolicyTenant());
			update = true;
		}

		return update;
	}

	public SecurityPolicy updateSecurityPolicy(SecurityPolicyUpdate securityPolicyUpdate, SecurityContext securityContext) {
		SecurityPolicy SecurityPolicy = securityPolicyUpdate.getSecurityPolicy();
		if (updateSecurityPolicyNoMerge(securityPolicyUpdate, SecurityPolicy)) {
			securityPolicyRepository.merge(SecurityPolicy);
		}
		return SecurityPolicy;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityPolicyRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends SecurityPolicy> T getSecurityPolicyByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityPolicyRepository.getSecurityPolicyByIdOrNull(id, c, securityContext);
	}

	public PaginationResponse<SecurityPolicy> getAllSecurityPolicies(SecurityPolicyFilter SecurityPolicyFilter, SecurityContext securityContext) {
		List<SecurityPolicy> list = listAllSecurityPolicies(SecurityPolicyFilter, securityContext);
		long count = securityPolicyRepository.countAllSecurityPolicies(SecurityPolicyFilter, securityContext);
		return new PaginationResponse<>(list, SecurityPolicyFilter, count);
	}

	public List<SecurityPolicy> listAllSecurityPolicies(SecurityPolicyFilter SecurityPolicyFilter, SecurityContext securityContext) {
		return securityPolicyRepository.listAllSecurityPolicies(SecurityPolicyFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securityPolicyRepository.findByIds(c, requested);
	}
}
