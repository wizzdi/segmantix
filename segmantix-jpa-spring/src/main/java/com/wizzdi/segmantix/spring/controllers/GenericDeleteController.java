package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;

import com.wizzdi.segmantix.spring.response.DeleteResponse;
import com.wizzdi.segmantix.spring.service.GenericDeleteService;
import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.DeleteObjectsRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OperationsInside
@RequestMapping("/generic")

public class GenericDeleteController  {

    @Autowired
    private GenericDeleteService genericDeleteService;

    @IOperation(Name = "soft deletes Objects", Description = "soft deletes Objects")
    @DeleteMapping("/softDelete")
    public DeleteResponse softDelete(@RequestBody DeleteObjectsRequest deleteObjectsRequest, @RequestAttribute SecurityContext securityContext) {
        genericDeleteService.validate(deleteObjectsRequest, securityContext);
        return genericDeleteService.softDelete(deleteObjectsRequest, securityContext);
    }

}
