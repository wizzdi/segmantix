package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.InstanceGroup;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupDuplicate;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.InstanceGroupUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.InstanceGroupService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/instanceGroup")

public class InstanceGroupController  {

    @Autowired
    private InstanceGroupService instanceGroupService;

    @IOperation(Name = "returns InstanceGroup", Description = "returns InstanceGroup")
    @PostMapping("/create")
    public InstanceGroup create(@RequestBody @Validated(Create.class) InstanceGroupCreate instanceGroupCreate, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupService.createInstanceGroup(instanceGroupCreate, securityContext);
    }

    @IOperation(Name = "returns InstanceGroup", Description = "returns InstanceGroup")
    @PostMapping("/getAll")
    public PaginationResponse<InstanceGroup> getAll(@RequestBody @Valid InstanceGroupFilter instanceGroupFilter, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupService.getAllInstanceGroups(instanceGroupFilter, securityContext);
    }

    @IOperation(Name = "updates InstanceGroup", Description = "updates InstanceGroup")
    @PutMapping("/update")
    public InstanceGroup update(@RequestBody @Validated(Update.class) InstanceGroupUpdate instanceGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupService.updateInstanceGroup(instanceGroupUpdate, securityContext);
    }

    @IOperation(Name = "duplicates InstanceGroup", Description = "duplicates InstanceGroup")
    @PutMapping("/duplicate")
    public InstanceGroup duplicate(@RequestBody @Valid InstanceGroupDuplicate instanceGroupDuplicate, @RequestAttribute SecurityContext securityContext) {

        return instanceGroupService.duplicate(instanceGroupDuplicate, securityContext);
    }
}
