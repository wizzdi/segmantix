package com.wizzdi.segmantix.spring.controllers;

import com.wizzdi.segmantix.spring.annotations.IOperation;
import com.wizzdi.segmantix.spring.annotations.OperationsInside;
import com.wizzdi.segmantix.store.jpa.model.SecurityLink;

import com.wizzdi.segmantix.store.jpa.service.SecurityContext;
import com.wizzdi.segmantix.spring.request.SecurityLinkFilter;
import com.wizzdi.segmantix.spring.request.SecurityLinkUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import com.wizzdi.segmantix.store.jpa.service.SecurityLinkService;
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
@RequestMapping("/securityLink")

public class SecurityLinkController  {

    @Autowired
    private SecurityLinkService securityLinkService;


    @IOperation(Name = "returns SecurityLink", Description = "returns SecurityLink")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityLink> getAll(@RequestBody @Valid SecurityLinkFilter securityLinkFilter, @RequestAttribute SecurityContext securityContext) {
        com.wizzdi.segmantix.store.jpa.request.SecurityLinkFilter securityLinkFilterService = securityLinkFilter.forService();
        securityLinkService.setRelevant(securityLinkFilterService,securityContext);
        return securityLinkService.getAllSecurityLinks(securityLinkFilterService, securityContext);
    }

    @IOperation(Name = "updates SecurityLink", Description = "updates SecurityLink")
    @PutMapping("/update")
    public SecurityLink update(@RequestBody @Validated(Update.class) SecurityLinkUpdate securityLinkUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityLinkService.updateSecurityLink(securityLinkUpdate.forService(), securityContext);
    }
}
