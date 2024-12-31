package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Clazz;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.BaseclassFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.BaseclassService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OperationsInside
@RequestMapping("/baseclass")

public class BaseclassController  {

    @Autowired
    private BaseclassService baseclassService;


    @IOperation(Name = "returns Baseclass", Description = "returns Baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<?> getAll(@RequestBody @Valid BaseclassFilter baseclassFilter, @RequestAttribute SecurityContext securityContext) {
        if(baseclassFilter.getClazzes()!=null){
            Clazz baseclazz=Clazz.ofClass(Baseclass.class);
            baseclassFilter.setClazzes(baseclassFilter.getClazzes().stream().filter(f->!baseclazz.equals(f)).toList());
        }
        return baseclassService.getAllBaseclass(baseclassFilter.forService(), securityContext);
    }

}
