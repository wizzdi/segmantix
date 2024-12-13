package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationToClazz;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.OperationToClazzService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/operationToClazz")

public class OperationToClazzController  {

    @Autowired
    private OperationToClazzService operationToClazzService;

    @IOperation(Name = "creates OperationToClazz", Description = "creates OperationToClazz")
    @PostMapping("/create")
    public OperationToClazz create(@RequestBody @Validated(Create.class) OperationToClazzCreate operationToClazzCreate, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.createOperationToClazz(operationToClazzCreate, securityContext);
    }

    @IOperation(Name = "returns OperationToClazz", Description = "returns OperationToClazz")
    @PostMapping("/getAll")
    public PaginationResponse<OperationToClazz> getAll(@RequestBody @Valid OperationToClazzFilter operationToClazzFilter, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.getAllOperationToClazz(operationToClazzFilter, securityContext);
    }

    @IOperation(Name = "updates OperationToClazz", Description = "updates OperationToClazz")
    @PutMapping("/update")
    public OperationToClazz update(@RequestBody @Validated(Update.class) OperationToClazzUpdate operationToClazzUpdate, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.updateOperationToClazz(operationToClazzUpdate, securityContext);
    }
}
