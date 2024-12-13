package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.RoleToUser;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleToUserUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.RoleToUserService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/roleToUser")

public class RoleToUserController  {

    @Autowired
    private RoleToUserService roleToUserService;

    @IOperation(Name = "creates RoleToUser", Description = "creates RoleToUser")
    @PostMapping("/create")
    public RoleToUser create(@RequestBody @Validated(Create.class) RoleToUserCreate roleToUserCreate, @RequestAttribute SecurityContext securityContext) {

        return roleToUserService.createRoleToUser(roleToUserCreate, securityContext);
    }

    @IOperation(Name = "returns RoleToUser", Description = "returns RoleToUser")
    @PostMapping("/getAll")
    public PaginationResponse<RoleToUser> getAll(@RequestBody @Valid RoleToUserFilter roleToUserFilter, @RequestAttribute SecurityContext securityContext) {

        return roleToUserService.getAllRoleToUsers(roleToUserFilter, securityContext);
    }

    @IOperation(Name = "updates RoleToUser", Description = "updates RoleToUser")
    @PutMapping("/update")
    public RoleToUser update(@RequestBody @Validated(Update.class) RoleToUserUpdate roleToUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleToUserService.updateRoleToUser(roleToUserUpdate, securityContext);
    }
}
