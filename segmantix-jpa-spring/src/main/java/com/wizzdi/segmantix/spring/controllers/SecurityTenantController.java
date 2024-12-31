package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.SecurityTenant;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.SecurityTenantCreate;
import com.wizzdi.segmantix.spring.request.SecurityTenantFilter;
import com.wizzdi.segmantix.spring.request.SecurityTenantUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.SecurityTenantService;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OperationsInside
@RequestMapping("/securityTenant")

public class SecurityTenantController  {

    @Autowired
    private SecurityTenantService tenantService;

    @IOperation(Name = "creates security tenant", Description = "creates security tenant")
    @PostMapping("/create")
    public SecurityTenant create(@RequestBody @Validated(Create.class) SecurityTenantCreate tenantCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantService.createTenant(tenantCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns security tenant", Description = "returns security tenant")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityTenant> getAll(@RequestBody @Valid SecurityTenantFilter tenantFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantService.getAllTenants(tenantFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates security tenant", Description = "updates security tenant")
    @PutMapping("/update")
    public SecurityTenant update(@RequestBody @Validated(Update.class) SecurityTenantUpdate tenantUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantService.updateTenant(tenantUpdate.forService(), securityContext);
    }
}
