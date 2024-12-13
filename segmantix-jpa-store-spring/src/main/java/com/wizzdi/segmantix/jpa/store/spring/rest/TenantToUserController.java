package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.TenantToUser;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.TenantToUserUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.TenantToUserService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/tenantToUser")

public class TenantToUserController  {

    @Autowired
    private TenantToUserService tenantToUserService;

    @IOperation(Name = "create tenant to user", Description = "creates tenant to user")
    @PostMapping("/create")
    public TenantToUser create(@RequestBody @Validated(Create.class) TenantToUserCreate tenantToUserCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.createTenantToUser(tenantToUserCreate, securityContext);
    }

    @IOperation(Name = "get all tenant to user", Description = "get all tenant to user")
    @PostMapping("/getAll")
    public PaginationResponse<TenantToUser> getAll(@RequestBody @Valid TenantToUserFilter tenantToUserFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.getAllTenantToUsers(tenantToUserFilter, securityContext);
    }

    @IOperation(Name = "updates tenant to user", Description = "updates tenant to user")
    @PutMapping("/update")
    public TenantToUser update(@RequestBody @Validated(Update.class) TenantToUserUpdate tenantToUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.updateTenantToUser(tenantToUserUpdate, securityContext);
    }
}
