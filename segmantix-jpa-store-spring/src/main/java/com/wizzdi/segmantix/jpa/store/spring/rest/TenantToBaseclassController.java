package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.TenantSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantSecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.TenantSecurityService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/tenantSecurity")

public class TenantSecurityController  {

    @Autowired
    private TenantSecurityService tenantSecurityService;

    @IOperation(Name = "creates tenant to baseclass", Description = "creates tenant to baseclass")
    @PostMapping("/create")
    public TenantSecurity create(@RequestBody @Validated(Create.class) TenantSecurityCreate tenantSecurityCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantSecurityService.createTenantSecurity(tenantSecurityCreate, securityContext);
    }

    @IOperation(Name = "returns tenant to baseclass", Description = "returns tenant to baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<TenantSecurity> getAll(@RequestBody @Valid TenantSecurityFilter tenantSecurityFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantSecurityService.getAllTenantSecuritys(tenantSecurityFilter, securityContext);
    }

    @IOperation(Name = "update tenant to baseclass", Description = "update tenant to baseclass")
    @PutMapping("/update")
    public TenantSecurity update(@RequestBody @Validated(Update.class) TenantSecurityUpdate tenantSecurityUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantSecurityService.updateTenantSecurity(tenantSecurityUpdate, securityContext);
    }
}
