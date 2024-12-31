package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.OperationGroup;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.OperationGroupCreate;
import com.wizzdi.segmantix.spring.request.OperationGroupFilter;
import com.wizzdi.segmantix.spring.request.OperationGroupUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.OperationGroupService;
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
@RequestMapping("/operationGroup")

public class OperationGroupController  {

    @Autowired
    private OperationGroupService operationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationGroup create(@RequestBody @Validated(Create.class) OperationGroupCreate operationCreate, @RequestAttribute SecurityContext securityContext) {

        return operationService.createOperationGroup(operationCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationGroup> getAll(@RequestBody @Valid OperationGroupFilter operationFilter, @RequestAttribute SecurityContext securityContext) {

        return operationService.getAllOperationGroups(operationFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationGroup update(@RequestBody @Validated(Update.class) OperationGroupUpdate operationUpdate, @RequestAttribute SecurityContext securityContext) {

        return operationService.updateOperationGroup(operationUpdate.forService(), securityContext);
    }
}
