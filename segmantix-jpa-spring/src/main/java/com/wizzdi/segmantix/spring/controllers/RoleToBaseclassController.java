package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.RoleToBaseclass;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.RoleToBaseclassCreate;
import com.wizzdi.segmantix.spring.request.RoleToBaseclassFilter;
import com.wizzdi.segmantix.spring.request.RoleToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.RoleToBaseclassService;
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
@RequestMapping("/roleToBaseclass")

public class RoleToBaseclassController  {

    @Autowired
    private RoleToBaseclassService roleToBaseclassService;

    @IOperation(Name = "creates RoleToBaseclass", Description = "creates RoleToBaseclass")
    @PostMapping("/create")
    public RoleToBaseclass create(@RequestBody @Validated(Create.class) RoleToBaseclassCreate roleToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return roleToBaseclassService.createRoleToBaseclass(roleToBaseclassCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns RoleToBaseclass", Description = "returns RoleToBaseclass")
    @PostMapping("/getAll")
    public PaginationResponse<RoleToBaseclass> getAll(@RequestBody @Valid RoleToBaseclassFilter roleToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return roleToBaseclassService.getAllRoleToBaseclass(roleToBaseclassFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates RoleToBaseclass", Description = "updates RoleToBaseclass")
    @PutMapping("/update")
    public RoleToBaseclass update(@RequestBody @Validated(Update.class) RoleToBaseclassUpdate roleToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleToBaseclassService.updateRoleToBaseclass(roleToBaseclassUpdate.forService(), securityContext);
    }
}
