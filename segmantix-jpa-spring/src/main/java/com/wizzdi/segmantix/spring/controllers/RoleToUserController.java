package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.RoleToUser;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.RoleToUserCreate;
import com.wizzdi.segmantix.spring.request.RoleToUserFilter;
import com.wizzdi.segmantix.spring.request.RoleToUserUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.RoleToUserService;
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
@RequestMapping("/roleToUser")

public class RoleToUserController  {

    @Autowired
    private RoleToUserService roleToUserService;

    @IOperation(Name = "creates RoleToUser", Description = "creates RoleToUser")
    @PostMapping("/create")
    public RoleToUser create(@RequestBody @Validated(Create.class) RoleToUserCreate roleToUserCreate, @RequestAttribute SecurityContext securityContext) {

        return roleToUserService.createRoleToUser(roleToUserCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns RoleToUser", Description = "returns RoleToUser")
    @PostMapping("/getAll")
    public PaginationResponse<RoleToUser> getAll(@RequestBody @Valid RoleToUserFilter roleToUserFilter, @RequestAttribute SecurityContext securityContext) {

        return roleToUserService.getAllRoleToUsers(roleToUserFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates RoleToUser", Description = "updates RoleToUser")
    @PutMapping("/update")
    public RoleToUser update(@RequestBody @Validated(Update.class) RoleToUserUpdate roleToUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleToUserService.updateRoleToUser(roleToUserUpdate.forService(), securityContext);
    }
}
