package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Operation;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationFilter;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.OperationService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@OperationsInside
@RequestMapping("/operation")

public class OperationController  {

    @Autowired
    private OperationService operationService;

    @IOperation(Name = "returns security operation", Description = "returns security operation")
    @PostMapping("/getAll")
    public PaginationResponse<Operation> getAll(@RequestBody @Valid OperationFilter operationFilter, @RequestAttribute SecurityContext securityContext) {

        return operationService.getAllOperations(operationFilter, securityContext);
    }

}
