package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Clazz;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.ClazzFilter;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.ClazzService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@OperationsInside
@RequestMapping("/clazz")

public class ClazzController  {

    @Autowired
    private ClazzService ClazzService;


    @IOperation(Name = "returns Clazz", Description = "returns Clazz")
    @PostMapping("/getAll")
    public PaginationResponse<Clazz> getAll(@RequestBody @Valid ClazzFilter ClazzFilter, @RequestAttribute SecurityContext securityContext) {

        return ClazzService.getAllClazzs(ClazzFilter, securityContext);
    }
}
