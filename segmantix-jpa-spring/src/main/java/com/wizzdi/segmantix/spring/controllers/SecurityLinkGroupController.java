package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.SecurityLinkGroup;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.*;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.response.SecurityLinkGroupContainer;
import com.wizzdi.segmantix.store.jpa.service.SecurityLinkGroupService;
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
@RequestMapping("/securityLinkGroup")

public class SecurityLinkGroupController  {

    @Autowired
    private SecurityLinkGroupService securityLinkGroupService;


    @IOperation(Name = "returns SecurityLinkGroup", Description = "returns SecurityLinkGroup")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityLinkGroup> getAll(@RequestBody @Valid SecurityLinkGroupFilter securityLinkGroupFilter, @RequestAttribute SecurityContext securityContext) {
        return securityLinkGroupService.getAllSecurityLinkGroups(securityLinkGroupFilter.forService(), securityContext);
    }

    @IOperation(Name = "returns SecurityLinkGroupContainers", Description = "returns SecurityLinkGroupContainers")
    @PostMapping("/getAllContainers")
    public PaginationResponse<SecurityLinkGroupContainer> getAllContainers(@RequestBody @Valid SecurityLinkGroupFilter securityLinkGroupFilter, @RequestAttribute SecurityContext securityContext) {
        return securityLinkGroupService.getAllSecurityLinkGroupContainers(securityLinkGroupFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates SecurityLinkGroup", Description = "updates SecurityLinkGroup")
    @PutMapping("/update")
    public SecurityLinkGroup update(@RequestBody @Validated(Update.class) SecurityLinkGroupUpdate securityLinkGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityLinkGroupService.updateSecurityLinkGroup(securityLinkGroupUpdate.forService(), securityContext);
    }

    @IOperation(Name = "creates SecurityLinkGroup", Description = "creates SecurityLinkGroup")
    @PostMapping("/create")
    public SecurityLinkGroup create(@RequestBody @Validated(Create.class) SecurityLinkGroupCreate securityLinkGroupCreate, @RequestAttribute SecurityContext securityContext) {

        return securityLinkGroupService.createSecurityLinkGroup(securityLinkGroupCreate.forService(), securityContext);
    }


}
