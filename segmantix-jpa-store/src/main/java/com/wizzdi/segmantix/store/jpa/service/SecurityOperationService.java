package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.SecurityOperationRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityOperationCreate;
import com.wizzdi.segmantix.store.jpa.request.SecurityOperationFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import org.eclipse.persistence.internal.oxm.schema.model.All;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class SecurityOperationService implements SegmantixService {


    private SecurityOperationRepository operationRepository;


    public SecurityOperation addOperation(SecurityOperationCreate securityOperationCreate) {
        SecurityOperation securityOperation = getSecurityOperation(securityOperationCreate);
        return operationRepository.addOperation(securityOperation);
    }

    public static SecurityOperation getSecurityOperation(SecurityOperationCreate securityOperationCreate) {
        return new SecurityOperation(securityOperationCreate.getMethod(), securityOperationCreate.getClazz(), securityOperationCreate.getIdForCreate(), securityOperationCreate.getName(), securityOperationCreate.getDescription(), securityOperationCreate.getDefaultAccess(), securityOperationCreate.getCategory());
    }


    public PaginationResponse<SecurityOperation> getAllOperations(SecurityOperationFilter operationFilter) {
        List<SecurityOperation> list = listAllOperations(operationFilter);
        long count = operationRepository.countAllOperations(operationFilter);
        return new PaginationResponse<>(list, operationFilter, count);
    }

    public List<SecurityOperation> listAllOperations(SecurityOperationFilter operationFilter) {
        return operationRepository.listAllOperations(operationFilter);
    }

    public SecurityOperation getByIdOrNull(String operationId) {
        return operationRepository.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(operationId)))).stream().findFirst().orElse(null);
    }

    public SecurityOperation getAllOperations() {
        return getByIdOrNull(getStandardAccessId(All.class));
    }

    public SecurityOperation getOperation(Method method) {
        return getByIdOrNull(getOperationId(method));
    }

    public List<SecurityOperation> findByIds(Set<String> ids) {
        return operationRepository.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(ids)));
    }

    public static String getStandardAccessId(Class<?> c) {
        return getOperationId(c.getCanonicalName());
    }

    public static String getOperationId(String input) {

        return UUID.nameUUIDFromBytes(input.getBytes()).toString();

    }

    public static String getOperationId(Method method) {
        return UUID.nameUUIDFromBytes(method.toString().getBytes()).toString();

    }


}
