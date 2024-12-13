package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationGroup;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.OperationGroupService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/operationGroup")

public class OperationGroupController  {

    @Autowired
    private OperationGroupService operationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationGroup create(@RequestBody @Validated(Create.class) OperationGroupCreate operationCreate, @RequestAttribute SecurityContext securityContext) {

        return operationService.createOperationGroup(operationCreate, securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationGroup> getAll(@RequestBody @Valid OperationGroupFilter operationFilter, @RequestAttribute SecurityContext securityContext) {

        return operationService.getAllOperationGroups(operationFilter, securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationGroup update(@RequestBody @Validated(Update.class) OperationGroupUpdate operationUpdate, @RequestAttribute SecurityContext securityContext) {

        return operationService.updateOperationGroup(operationUpdate, securityContext);
    }
}
