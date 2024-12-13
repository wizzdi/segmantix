package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityGroup;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.*;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.response.SecurityGroupContainer;
import com.wizzdi.segmantix.jpa.store.spring.service.SecurityGroupService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/securityGroup")

public class SecurityGroupController  {

    @Autowired
    private SecurityGroupService securityGroupService;


    @IOperation(Name = "returns SecurityGroup", Description = "returns SecurityGroup")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityGroup> getAll(@RequestBody @Valid SecurityGroupFilter securityGroupFilter, @RequestAttribute SecurityContext securityContext) {
        return securityGroupService.getAllSecurityGroups(securityGroupFilter, securityContext);
    }

    @IOperation(Name = "returns SecurityGroupContainers", Description = "returns SecurityGroupContainers")
    @PostMapping("/getAllContainers")
    public PaginationResponse<SecurityGroupContainer> getAllContainers(@RequestBody @Valid SecurityGroupFilter securityGroupFilter, @RequestAttribute SecurityContext securityContext) {
        return securityGroupService.getAllSecurityGroupContainers(securityGroupFilter, securityContext);
    }

    @IOperation(Name = "updates SecurityGroup", Description = "updates SecurityGroup")
    @PutMapping("/update")
    public SecurityGroup update(@RequestBody @Validated(Update.class) SecurityGroupUpdate securityGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityGroupService.updateSecurityGroup(securityGroupUpdate, securityContext);
    }

    @IOperation(Name = "creates SecurityGroup", Description = "creates SecurityGroup")
    @PostMapping("/create")
    public SecurityGroup create(@RequestBody @Validated(Create.class) SecurityGroupCreate securityGroupCreate, @RequestAttribute SecurityContext securityContext) {

        return securityGroupService.createSecurityGroup(securityGroupCreate, securityContext);
    }


}
