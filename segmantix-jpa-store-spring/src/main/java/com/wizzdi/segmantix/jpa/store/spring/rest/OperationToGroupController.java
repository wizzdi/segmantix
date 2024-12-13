package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationGroupLink;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.OperationGroupLinkService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/operationGroupLink")

public class OperationGroupLinkController  {

    @Autowired
    private OperationGroupLinkService operationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationGroupLink create(@RequestBody @Validated(Create.class) OperationGroupLinkCreate operationGroupLinkCreate, @RequestAttribute SecurityContext securityContext) {

        return operationService.createOperationGroupLink(operationGroupLinkCreate, securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationGroupLink> getAll(@RequestBody @Valid OperationGroupLinkFilter operationGroupLinkFilter, @RequestAttribute SecurityContext securityContext) {

        return operationService.getAllOperationGroupLinks(operationGroupLinkFilter, securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationGroupLink update(@RequestBody @Validated(Update.class) OperationGroupLinkUpdate operationGroupLinkUpdate, @RequestAttribute SecurityContext securityContext) {

        return operationService.updateOperationGroupLink(operationGroupLinkUpdate, securityContext);
    }
}
