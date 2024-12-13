package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.RoleSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.RoleSecurityCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleSecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.RoleSecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.RoleSecurityService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/roleSecurity")

public class RoleSecurityController  {

    @Autowired
    private RoleSecurityService roleSecurityService;

    @IOperation(Name = "creates RoleSecurity", Description = "creates RoleSecurity")
    @PostMapping("/create")
    public RoleSecurity create(@RequestBody @Validated(Create.class) RoleSecurityCreate roleSecurityCreate, @RequestAttribute SecurityContext securityContext) {

        return roleSecurityService.createRoleSecurity(roleSecurityCreate, securityContext);
    }

    @IOperation(Name = "returns RoleSecurity", Description = "returns RoleSecurity")
    @PostMapping("/getAll")
    public PaginationResponse<RoleSecurity> getAll(@RequestBody @Valid RoleSecurityFilter roleSecurityFilter, @RequestAttribute SecurityContext securityContext) {

        return roleSecurityService.getAllRoleSecurity(roleSecurityFilter, securityContext);
    }

    @IOperation(Name = "updates RoleSecurity", Description = "updates RoleSecurity")
    @PutMapping("/update")
    public RoleSecurity update(@RequestBody @Validated(Update.class) RoleSecurityUpdate roleSecurityUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleSecurityService.updateRoleSecurity(roleSecurityUpdate, securityContext);
    }
}
