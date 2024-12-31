package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.TenantToUser;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.TenantToUserCreate;
import com.wizzdi.segmantix.spring.request.TenantToUserFilter;
import com.wizzdi.segmantix.spring.request.TenantToUserUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.TenantToUserService;
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
@RequestMapping("/tenantToUser")

public class TenantToUserController  {

    @Autowired
    private TenantToUserService tenantToUserService;

    @IOperation(Name = "create tenant to user", Description = "creates tenant to user")
    @PostMapping("/create")
    public TenantToUser create(@RequestBody @Validated(Create.class) TenantToUserCreate tenantToUserCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.createTenantToUser(tenantToUserCreate.forService(), securityContext);
    }

    @IOperation(Name = "get all tenant to user", Description = "get all tenant to user")
    @PostMapping("/getAll")
    public PaginationResponse<TenantToUser> getAll(@RequestBody @Valid TenantToUserFilter tenantToUserFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.getAllTenantToUsers(tenantToUserFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates tenant to user", Description = "updates tenant to user")
    @PutMapping("/update")
    public TenantToUser update(@RequestBody @Validated(Update.class) TenantToUserUpdate tenantToUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.updateTenantToUser(tenantToUserUpdate.forService(), securityContext);
    }
}
