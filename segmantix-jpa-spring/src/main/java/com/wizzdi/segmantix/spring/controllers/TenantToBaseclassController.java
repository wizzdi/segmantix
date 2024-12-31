package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.TenantToBaseclass;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.TenantToBaseclassCreate;
import com.wizzdi.segmantix.spring.request.TenantToBaseclassFilter;
import com.wizzdi.segmantix.spring.request.TenantToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.TenantToBaseclassService;
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
@RequestMapping("/tenantToBaseclass")

public class TenantToBaseclassController  {

    @Autowired
    private TenantToBaseclassService tenantToBaseclassService;

    @IOperation(Name = "creates tenant to baseclass", Description = "creates tenant to baseclass")
    @PostMapping("/create")
    public TenantToBaseclass create(@RequestBody @Validated(Create.class) TenantToBaseclassCreate tenantToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantToBaseclassService.createTenantToBaseclass(tenantToBaseclassCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns tenant to baseclass", Description = "returns tenant to baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<TenantToBaseclass> getAll(@RequestBody @Valid TenantToBaseclassFilter tenantToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantToBaseclassService.getAllTenantToBaseclasss(tenantToBaseclassFilter.forService(), securityContext);
    }

    @IOperation(Name = "update tenant to baseclass", Description = "update tenant to baseclass")
    @PutMapping("/update")
    public TenantToBaseclass update(@RequestBody @Validated(Update.class) TenantToBaseclassUpdate tenantToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantToBaseclassService.updateTenantToBaseclass(tenantToBaseclassUpdate.forService(), securityContext);
    }
}
