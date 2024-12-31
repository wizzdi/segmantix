package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroup;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.PermissionGroupCreate;
import com.wizzdi.segmantix.spring.request.PermissionGroupDuplicate;
import com.wizzdi.segmantix.spring.request.PermissionGroupFilter;
import com.wizzdi.segmantix.spring.request.PermissionGroupUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.PermissionGroupService;
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
@RequestMapping("/permissionGroup")

public class PermissionGroupController  {

    @Autowired
    private PermissionGroupService permissionGroupService;

    @IOperation(Name = "returns PermissionGroup", Description = "returns PermissionGroup")
    @PostMapping("/create")
    public PermissionGroup create(@RequestBody @Validated(Create.class) PermissionGroupCreate permissionGroupCreate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupService.createPermissionGroup(permissionGroupCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns PermissionGroup", Description = "returns PermissionGroup")
    @PostMapping("/getAll")
    public PaginationResponse<PermissionGroup> getAll(@RequestBody @Valid PermissionGroupFilter permissionGroupFilter, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupService.getAllPermissionGroups(permissionGroupFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates PermissionGroup", Description = "updates PermissionGroup")
    @PutMapping("/update")
    public PermissionGroup update(@RequestBody @Validated(Update.class) PermissionGroupUpdate permissionGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupService.updatePermissionGroup(permissionGroupUpdate.forService(), securityContext);
    }

    @IOperation(Name = "duplicates PermissionGroup", Description = "duplicates PermissionGroup")
    @PutMapping("/duplicate")
    public PermissionGroup duplicate(@RequestBody @Valid PermissionGroupDuplicate permissionGroupDuplicate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupService.duplicate(permissionGroupDuplicate.forService(), securityContext);
    }
}
