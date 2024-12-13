package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Tenant;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.TenantCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.TenantService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/tenant")

public class TenantController  {

    @Autowired
    private TenantService tenantService;

    @IOperation(Name = "creates security tenant", Description = "creates security tenant")
    @PostMapping("/create")
    public Tenant create(@RequestBody @Validated(Create.class) TenantCreate tenantCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantService.createTenant(tenantCreate, securityContext);
    }

    @IOperation(Name = "returns security tenant", Description = "returns security tenant")
    @PostMapping("/getAll")
    public PaginationResponse<Tenant> getAll(@RequestBody @Valid TenantFilter tenantFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantService.getAllTenants(tenantFilter, securityContext);
    }

    @IOperation(Name = "updates security tenant", Description = "updates security tenant")
    @PutMapping("/update")
    public Tenant update(@RequestBody @Validated(Update.class) TenantUpdate tenantUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantService.updateTenant(tenantUpdate, securityContext);
    }
}
