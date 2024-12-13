package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.DeleteObjectsRequest;
import com.wizzdi.segmantix.jpa.store.spring.response.DeleteResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.GenericDeleteService;

import org.springframework.beans.factory.annotation.Autowired;

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
