package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Security;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.SecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.SecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.SecurityService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/security")

public class SecurityController  {

    @Autowired
    private SecurityService securityService;


    @IOperation(Name = "returns Security", Description = "returns Security")
    @PostMapping("/getAll")
    public PaginationResponse<Security> getAll(@RequestBody @Valid SecurityFilter securityFilter, @RequestAttribute SecurityContext securityContext) {
        securityService.setRelevant(securityFilter,securityContext);
        return securityService.getAllSecuritys(securityFilter, securityContext);
    }

    @IOperation(Name = "updates Security", Description = "updates Security")
    @PutMapping("/update")
    public Security update(@RequestBody @Validated(Update.class) SecurityUpdate securityUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityService.updateSecurity(securityUpdate, securityContext);
    }
}
