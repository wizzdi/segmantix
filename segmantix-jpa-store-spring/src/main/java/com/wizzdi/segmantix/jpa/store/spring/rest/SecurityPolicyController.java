package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.security.SecurityPolicy;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityPolicyUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.SecurityPolicyService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/securityPolicy")

public class SecurityPolicyController  {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @IOperation(Name = "creates security policy", Description = "creates security policy")
    @PostMapping("/create")
    public SecurityPolicy create(@RequestBody @Validated(Create.class) SecurityPolicyCreate securityPolicyCreate, @RequestAttribute SecurityContext securityContext) {

        return securityPolicyService.createSecurityPolicy(securityPolicyCreate, securityContext);
    }

    @IOperation(Name = "returns security policy", Description = "returns security policy")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityPolicy> getAll(@RequestBody @Valid SecurityPolicyFilter securityPolicyFilter, @RequestAttribute SecurityContext securityContext) {

        return securityPolicyService.getAllSecurityPolicies(securityPolicyFilter, securityContext);
    }

    @IOperation(Name = "updates security policy", Description = "updates security policy")
    @PutMapping("/update")
    public SecurityPolicy update(@RequestBody @Validated(Update.class) SecurityPolicyUpdate securityPolicyUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityPolicyService.updateSecurityPolicy(securityPolicyUpdate, securityContext);
    }
}
