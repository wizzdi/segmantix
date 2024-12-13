package com.wizzdi.segmantix.jpa.store.spring.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Baseclass;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.BaseclassFilter;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import com.wizzdi.segmantix.jpa.store.spring.service.BaseclassService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@OperationsInside
@RequestMapping("/baseclass")

public class BaseclassController  {

    @Autowired
    private BaseclassService baseclassService;


    @IOperation(Name = "returns Baseclass", Description = "returns Baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<Baseclass> getAll(@RequestBody @Valid BaseclassFilter baseclassFilter, @RequestAttribute SecurityContext securityContext) {
        if(baseclassFilter.getClazzes()!=null){
            baseclassFilter.setClazzes(baseclassFilter.getClazzes().stream().filter(f->!Baseclass.class.getCanonicalName().equals(f.getName())).toList());
        }
        return baseclassService.getAllBaseclass(baseclassFilter, securityContext);
    }

}
