package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.Operation;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.OperationRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class OperationService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private OperationRepository operationRepository;


	public Operation createOperation(OperationCreate operationCreate, SecurityContext securityContext){
		Operation operation= createOperationNoMerge(operationCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public Operation createOperationNoMerge(OperationCreate operationCreate, SecurityContext securityContext){
		Operation operation=new Operation();
		operation.setId(UUID.randomUUID().toString());
		updateOperationNoMerge(operationCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationNoMerge(OperationCreate operationCreate, Operation operation) {
		boolean update = basicService.updateBasicNoMerge(operationCreate, operation);
		if(operationCreate.getCategory()!=null&&!operationCreate.getCategory().equals(operation.getCategory())){
			operation.setCategory(operationCreate.getCategory());
			update=true;
		}
		return update;
	}

	public Operation updateOperation(OperationUpdate operationUpdate, SecurityContext securityContext){
		Operation operation=operationUpdate.getOperation();
		if(updateOperationNoMerge(operationUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}



	public PaginationResponse<Operation> getAllOperations(OperationFilter operationFilter, SecurityContext securityContext) {
		List<Operation> list= listAllOperations(operationFilter, securityContext);
		long count=operationRepository.countAllOperations(operationFilter,securityContext);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<Operation> listAllOperations(OperationFilter operationFilter, SecurityContext securityContext) {
		return operationRepository.listAllOperations(operationFilter, securityContext);
	}


	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return operationRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		operationRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return operationRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return operationRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return operationRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return operationRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return operationRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return operationRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return operationRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return operationRepository.merge(base);
	}


	public void massMerge(List<?> toMerge) {
		operationRepository.massMerge(toMerge);
	}
}
