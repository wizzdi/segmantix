package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.Clazz;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.ClazzFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.ClazzService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OperationsInside
@RequestMapping("/clazz")

public class ClazzController  {

    @Autowired
    private ClazzService ClazzService;


    @IOperation(Name = "returns Clazz", Description = "returns Clazz")
    @PostMapping("/getAll")
    public PaginationResponse<Clazz> getAll(@RequestBody @Valid ClazzFilter clazzFilter, @RequestAttribute SecurityContext securityContext) {

        return ClazzService.getAllClazzs(clazzFilter.forService());
    }
}
