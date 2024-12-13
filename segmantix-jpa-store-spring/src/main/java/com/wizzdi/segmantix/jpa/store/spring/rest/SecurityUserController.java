package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.User;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.UserCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.UserFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.UserUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.UserService;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/user")
@OperationsInside

public class UserController  {

    @Autowired
    private UserService userService;

    @IOperation(Name = "creates security user", Description = "creates security user")
    @PostMapping("/create")
    public User create(@RequestBody @Validated(Create.class) UserCreate userCreate, @RequestAttribute SecurityContext securityContext) {

        return userService.createUser(userCreate, securityContext);
    }

    @IOperation(Name = "returns security user", Description = "returns security user")
    @PostMapping("/getAll")
    public PaginationResponse<User> getAll(@RequestBody @Valid UserFilter userFilter, @RequestAttribute SecurityContext securityContext) {

        return userService.getAllUsers(userFilter, securityContext);
    }

    @IOperation(Name = "updates security user", Description = "updates security user")
    @PutMapping("/update")
    public User update(@RequestBody @Validated(Update.class) UserUpdate userUpdate, @RequestAttribute SecurityContext securityContext) {

        return userService.updateUser(userUpdate, securityContext);
    }
}
