package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.OperationToClazz;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.OperationToClazzCreate;
import com.wizzdi.segmantix.spring.request.OperationToClazzFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.OperationToClazzService;
import com.wizzdi.segmantix.spring.validation.Create;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OperationsInside
@RequestMapping("/operationToClazz")

public class OperationToClazzController  {

    @Autowired
    private OperationToClazzService operationToClazzService;

    @IOperation(Name = "creates OperationToClazz", Description = "creates OperationToClazz")
    @PostMapping("/create")
    public OperationToClazz create(@RequestBody @Validated(Create.class) OperationToClazzCreate operationToClazzCreate, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.addOperationToClazz(operationToClazzCreate.forService());
    }

    @IOperation(Name = "returns OperationToClazz", Description = "returns OperationToClazz")
    @PostMapping("/getAll")
    public PaginationResponse<OperationToClazz> getAll(@RequestBody @Valid OperationToClazzFilter operationToClazzFilter, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.getAllOperationToClazz(operationToClazzFilter.forService());
    }


}
