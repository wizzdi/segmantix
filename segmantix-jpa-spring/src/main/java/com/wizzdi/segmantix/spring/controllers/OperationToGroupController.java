package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityOperationFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.OperationToGroupCreate;
import com.wizzdi.segmantix.spring.request.OperationToGroupFilter;
import com.wizzdi.segmantix.spring.request.OperationToGroupUpdate;
import com.wizzdi.segmantix.store.jpa.response.OperationToGroupContainer;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.OperationToGroupService;
import com.wizzdi.segmantix.store.jpa.service.SecurityOperationService;
import com.wizzdi.segmantix.spring.validation.Create;
import com.wizzdi.segmantix.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@OperationsInside
@RequestMapping("/operationToGroup")

public class OperationToGroupController {

    @Autowired
    private OperationToGroupService operationService;
    @Autowired
    private SecurityOperationService securityOperationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationToGroup create(@RequestBody @Validated(Create.class) OperationToGroupCreate operationToGroupCreate, @RequestAttribute SecurityContext securityContext) {
        String operationId = operationToGroupCreate.getOperationId();
        SecurityOperation securityOperation = securityOperationService.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(operationId)))).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no operation with id %s".formatted(operationId)));
        operationToGroupCreate.setOperation(securityOperation);
        return operationService.createOperationToGroup(operationToGroupCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationToGroup> getAll(@RequestBody @Valid OperationToGroupFilter operationToGroupFilter, @RequestAttribute SecurityContext securityContext) {

        setOperations(operationToGroupFilter);

        return operationService.getAllOperationToGroups(operationToGroupFilter.forService(), securityContext);
    }

    @IOperation(Name = "returns security operation group containers", Description = "returns security operation group containers")
    @PostMapping("/getAllContainers")
    public PaginationResponse<OperationToGroupContainer> getAllContainers(@RequestBody @Valid OperationToGroupFilter operationToGroupFilter, @RequestAttribute SecurityContext securityContext) {

        setOperations(operationToGroupFilter);

        return operationService.getAllOperationToGroupsContainers(operationToGroupFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationToGroup update(@RequestBody @Validated(Update.class) OperationToGroupUpdate operationToGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        if (operationToGroupUpdate.getOperationId() != null) {
            String operationId = operationToGroupUpdate.getOperationId();
            SecurityOperation securityOperation = securityOperationService.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(operationId)))).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no operation with id %s".formatted(operationId)));
            operationToGroupUpdate.setOperation(securityOperation);
        }
        return operationService.updateOperationToGroup(operationToGroupUpdate.forService(), securityContext);
    }

    public void setOperations(OperationToGroupFilter operationToGroupFilter) {
        Set<String> operationIds = new HashSet<>(operationToGroupFilter.getOperationIds());
        Map<String, SecurityOperation> operations = securityOperationService.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(operationIds))).stream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        operationIds.removeAll(operations.keySet());
        if (!operationIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no operations with ids %s".formatted(String.join(",", operationIds)));
        }
        operationToGroupFilter.setOperations(new ArrayList<>(operations.values()));
    }
}
