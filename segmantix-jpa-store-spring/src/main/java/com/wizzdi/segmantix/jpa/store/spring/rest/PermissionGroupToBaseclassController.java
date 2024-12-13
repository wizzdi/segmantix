package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.InstanceGroupLink;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupLinkUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.InstanceGroupLinkService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/instanceGroupLink")

public class InstanceGroupLinkController  {

    @Autowired
    private InstanceGroupLinkService instanceGroupLinkService;

    @IOperation(Name = "creates InstanceGroupLink", Description = "creates InstanceGroupLink")
    @PostMapping("/create")
    public InstanceGroupLink create(@RequestBody @Validated(Create.class) InstanceGroupLinkCreate instanceGroupLinkCreate, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupLinkService.createInstanceGroupLink(instanceGroupLinkCreate, securityContext);
    }

    @IOperation(Name = "returns InstanceGroupLink", Description = "returns InstanceGroupLink")
    @PostMapping("/getAll")
    public PaginationResponse<InstanceGroupLink> getAll(@RequestBody @Valid InstanceGroupLinkFilter instanceGroupLinkFilter, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupLinkService.getAllInstanceGroupLink(instanceGroupLinkFilter, securityContext);
    }

    @IOperation(Name = "updates InstanceGroupLink", Description = "updates InstanceGroupLink")
    @PutMapping("/update")
    public InstanceGroupLink update(@RequestBody @Validated(Update.class) InstanceGroupLinkUpdate instanceGroupLinkUpdate, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupLinkService.updateInstanceGroupLink(instanceGroupLinkUpdate, securityContext);
    }
}
