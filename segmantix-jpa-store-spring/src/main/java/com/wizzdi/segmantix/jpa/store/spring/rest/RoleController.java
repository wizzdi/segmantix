package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Role;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.RoleCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.RoleService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/role")

public class RoleController  {

    @Autowired
    private RoleService roleService;

    @IOperation(Name = "creates Role", Description = "creates Role")
    @PostMapping("/create")
    public Role create(@RequestBody @Validated(Create.class) RoleCreate roleCreate, @RequestAttribute SecurityContext securityContext) {

        return roleService.createRole(roleCreate, securityContext);
    }

    @IOperation(Name = "returns Role", Description = "returns Role")
    @PostMapping("/getAll")
    public PaginationResponse<Role> getAll(@RequestBody @Valid RoleFilter roleFilter, @RequestAttribute SecurityContext securityContext) {

        return roleService.getAllRoles(roleFilter, securityContext);
    }

    @IOperation(Name = "updates Role", Description = "updates Role")
    @PutMapping("/update")
    public Role update(@RequestBody @Validated(Update.class) RoleUpdate roleUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleService.updateRole(roleUpdate, securityContext);
    }
}
