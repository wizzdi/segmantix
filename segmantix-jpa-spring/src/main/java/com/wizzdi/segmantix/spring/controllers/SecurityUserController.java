package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.SecurityUser;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.SecurityUserCreate;
import com.wizzdi.segmantix.spring.request.SecurityUserFilter;
import com.wizzdi.segmantix.spring.request.SecurityUserUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.SecurityUserService;
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
@RequestMapping("/securityUser")
@OperationsInside

public class SecurityUserController  {

    @Autowired
    private SecurityUserService securityUserService;

    @IOperation(Name = "creates security user", Description = "creates security user")
    @PostMapping("/create")
    public SecurityUser create(@RequestBody @Validated(Create.class) SecurityUserCreate securityUserCreate, @RequestAttribute SecurityContext securityContext) {

        return securityUserService.createSecurityUser(securityUserCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns security user", Description = "returns security user")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityUser> getAll(@RequestBody @Valid SecurityUserFilter securityUserFilter, @RequestAttribute SecurityContext securityContext) {

        return securityUserService.getAllSecurityUsers(securityUserFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates security user", Description = "updates security user")
    @PutMapping("/update")
    public SecurityUser update(@RequestBody @Validated(Update.class) SecurityUserUpdate securityUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityUserService.updateSecurityUser(securityUserUpdate.forService(), securityContext);
    }
}
