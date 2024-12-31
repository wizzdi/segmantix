package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.Role;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.RoleCreate;
import com.wizzdi.segmantix.spring.request.RoleFilter;
import com.wizzdi.segmantix.spring.request.RoleUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.RoleService;
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
@RequestMapping("/role")

public class RoleController  {

    @Autowired
    private RoleService roleService;

    @IOperation(Name = "creates Role", Description = "creates Role")
    @PostMapping("/create")
    public Role create(@RequestBody @Validated(Create.class) RoleCreate roleCreate, @RequestAttribute SecurityContext securityContext) {

        return roleService.createRole(roleCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns Role", Description = "returns Role")
    @PostMapping("/getAll")
    public PaginationResponse<Role> getAll(@RequestBody @Valid RoleFilter roleFilter, @RequestAttribute SecurityContext securityContext) {

        return roleService.getAllRoles(roleFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates Role", Description = "updates Role")
    @PutMapping("/update")
    public Role update(@RequestBody @Validated(Update.class) RoleUpdate roleUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleService.updateRole(roleUpdate.forService(), securityContext);
    }
}
