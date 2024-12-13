package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.UserSecurity;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.UserSecurityUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.UserSecurityService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@OperationsInside
@RequestMapping("/userSecurity")

public class UserSecurityController  {

    @Autowired
    private UserSecurityService userSecurityService;

    @IOperation(Name = "create user to baseclass", Description = "creates user to baseclass")
    @PostMapping("/create")
    public UserSecurity create(@RequestBody @Validated(Create.class) UserSecurityCreate userSecurityCreate, @RequestAttribute SecurityContext securityContext) {

        return userSecurityService.createUserSecurity(userSecurityCreate, securityContext);
    }

    @IOperation(Name = "returns user to baseclass", Description = "returns user to baseclass")

    @PostMapping("/getAll")
    public PaginationResponse<UserSecurity> getAll(@RequestBody @Valid UserSecurityFilter userSecurityFilter, @RequestAttribute SecurityContext securityContext) {

        return userSecurityService.getAllUserSecurity(userSecurityFilter, securityContext);
    }

    @IOperation(Name = "updates user to baseclass", Description = "updates user to baseclass")

    @PutMapping("/update")
    public UserSecurity update(@RequestBody @Validated(Update.class) UserSecurityUpdate userSecurityUpdate, @RequestAttribute SecurityContext securityContext) {

        return userSecurityService.updateUserSecurity(userSecurityUpdate, securityContext);
    }
}
