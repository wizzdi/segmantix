package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.PermissionGroupToBaseclass;

import com.wizzdi.segmantix.store.jpa.request.BaseclassFilter;
import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.segmantix.spring.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.segmantix.spring.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.BaseclassService;
import com.wizzdi.segmantix.store.jpa.service.PermissionGroupToBaseclassService;
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

import java.util.Collections;
import java.util.Set;

@RestController
@OperationsInside
@RequestMapping("/permissionGroupToBaseclass")

public class PermissionGroupToBaseclassController  {

    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private BaseclassService baseclassService;

    @IOperation(Name = "creates PermissionGroupToBaseclass", Description = "creates PermissionGroupToBaseclass")
    @PostMapping("/create")
    public PermissionGroupToBaseclass create(@RequestBody @Validated(Create.class) PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {


            String securedId = permissionGroupToBaseclassCreate.getSecuredId();
            Clazz securedType = permissionGroupToBaseclassCreate.getSecuredType();
            String name = securedType.name();
            Object secured = baseclassService.listAllBaseclass(new BaseclassFilter().setClazzes(Collections.singletonList(securedType)).setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(securedId))), securityContext).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no instance of type %s and id %s".formatted(name, securedId)));
            if(secured instanceof Basic basic){
                if(permissionGroupToBaseclassCreate.getName()==null){
                    permissionGroupToBaseclassCreate.setName(basic.getName());
                }
                if(permissionGroupToBaseclassCreate.getSecuredCreationDate()==null){
                   permissionGroupToBaseclassCreate.setSecuredCreationDate(basic.getCreationDate());
                }
            }
        return permissionGroupToBaseclassService.createPermissionGroupToBaseclass(permissionGroupToBaseclassCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns PermissionGroupToBaseclass", Description = "returns PermissionGroupToBaseclass")
    @PostMapping("/getAll")
    public PaginationResponse<PermissionGroupToBaseclass> getAll(@RequestBody @Valid PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.getAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates PermissionGroupToBaseclass", Description = "updates PermissionGroupToBaseclass")
    @PutMapping("/update")
    public PermissionGroupToBaseclass update(@RequestBody @Validated(Update.class) PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.updatePermissionGroupToBaseclass(permissionGroupToBaseclassUpdate.forService(), securityContext);
    }
}
