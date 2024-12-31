package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.spring.request.SecurityOperationFilter;
import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.OperationToGroupFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.OperationToGroupService;
import com.wizzdi.segmantix.store.jpa.service.SecurityOperationService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@OperationsInside
@RequestMapping("/securityOperation")

public class SecurityOperationController  {

    @Autowired
    private SecurityOperationService operationService;
    @Autowired
    private OperationToGroupService operationToGroupService;

    @IOperation(Name = "returns security operation", Description = "returns security operation")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityOperation> getAll(@RequestBody @Valid SecurityOperationFilter operationFilter, @RequestAttribute SecurityContext securityContext) {

        com.wizzdi.segmantix.store.jpa.request.SecurityOperationFilter securityOperationFilter = operationFilter.forService();
        List<OperationGroup> operationGroups = operationFilter.getOperationGroups();
        if(operationGroups !=null&&!operationGroups.isEmpty()){
            BasicPropertiesFilter basicPropertiesFilter= Optional.ofNullable(operationFilter.getBasicPropertiesFilter()).map(f->f.forService()).orElseGet(()->new BasicPropertiesFilter());
            Set<String> onlyIds=Optional.of(basicPropertiesFilter).map(f->f.getOnlyIds()).orElseGet(()->new HashSet<>());
           onlyIds.addAll(operationToGroupService.listAllOperationToGroups(new OperationToGroupFilter().setOperationGroups(operationGroups),null).stream().map(f->f.getOperationId()).collect(Collectors.toSet()));
           basicPropertiesFilter.setOnlyIds(onlyIds);
           securityOperationFilter.setBasicPropertiesFilter(basicPropertiesFilter);
        }
        return operationService.getAllOperations(securityOperationFilter);
    }

}
