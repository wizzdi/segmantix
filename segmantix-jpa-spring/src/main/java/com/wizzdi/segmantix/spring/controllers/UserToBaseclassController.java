package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.UserToBaseclass;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.UserToBaseclassCreate;
import com.wizzdi.segmantix.spring.request.UserToBaseclassFilter;
import com.wizzdi.segmantix.spring.request.UserToBaseclassUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.UserToBaseclassService;
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
@RequestMapping("/userToBaseclass")

public class UserToBaseclassController  {

    @Autowired
    private UserToBaseclassService userToBaseclassService;

    @IOperation(Name = "create user to baseclass", Description = "creates user to baseclass")
    @PostMapping("/create")
    public UserToBaseclass create(@RequestBody @Validated(Create.class) UserToBaseclassCreate userToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return userToBaseclassService.createUserToBaseclass(userToBaseclassCreate.forService(), securityContext);
    }

    @IOperation(Name = "returns user to baseclass", Description = "returns user to baseclass")

    @PostMapping("/getAll")
    public PaginationResponse<UserToBaseclass> getAll(@RequestBody @Valid UserToBaseclassFilter userToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return userToBaseclassService.getAllUserToBaseclass(userToBaseclassFilter.forService(), securityContext);
    }

    @IOperation(Name = "updates user to baseclass", Description = "updates user to baseclass")

    @PutMapping("/update")
    public UserToBaseclass update(@RequestBody @Validated(Update.class) UserToBaseclassUpdate userToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return userToBaseclassService.updateUserToBaseclass(userToBaseclassUpdate.forService(), securityContext);
    }
}
