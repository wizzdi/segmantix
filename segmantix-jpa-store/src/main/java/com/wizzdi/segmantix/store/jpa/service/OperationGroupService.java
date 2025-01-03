package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.OperationGroupRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.model.OperationGroup;
import com.wizzdi.segmantix.store.jpa.request.OperationGroupCreate;
import com.wizzdi.segmantix.store.jpa.request.OperationGroupFilter;
import com.wizzdi.segmantix.store.jpa.request.OperationGroupUpdate;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;
import java.util.Set;
import java.util.UUID;



public class OperationGroupService implements SegmantixService {

	private final BasicService basicService;
	private final OperationGroupRepository operationRepository;

    public OperationGroupService(BasicService basicService, OperationGroupRepository operationRepository) {
        this.basicService = basicService;
        this.operationRepository = operationRepository;
    }


    public OperationGroup createOperationGroup(OperationGroupCreate operationCreate, SecurityContext securityContext){
		OperationGroup operation= createOperationGroupNoMerge(operationCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public OperationGroup createOperationGroupNoMerge(OperationGroupCreate operationCreate, SecurityContext securityContext){
		OperationGroup operation=new OperationGroup();
		operation.setId(UUID.randomUUID().toString());
		updateOperationGroupNoMerge(operationCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationGroupNoMerge(OperationGroupCreate operationCreate, OperationGroup operation) {
		boolean update = basicService.updateBasicNoMerge(operationCreate, operation);
		if(operationCreate.getExternalId()!=null&&!operationCreate.getExternalId().equals(operation.getExternalId())){
			operation.setExternalId(operationCreate.getExternalId());
			update=true;
		}
		return update;
	}

	public OperationGroup updateOperationGroup(OperationGroupUpdate operationUpdate, SecurityContext securityContext){
		OperationGroup operation=operationUpdate.getOperation();
		if(updateOperationGroupNoMerge(operationUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public OperationGroup updateOperationGroup(OperationGroupCreate operationUpdate, OperationGroup operationGroup){
		if(updateOperationGroupNoMerge(operationUpdate,operationGroup)){
			operationRepository.merge(operationGroup);
		}
		return operationGroup;
	}



	public PaginationResponse<OperationGroup> getAllOperationGroups(OperationGroupFilter operationFilter, SecurityContext securityContext) {
		List<OperationGroup> list= listAllOperationGroups(operationFilter, securityContext);
		long count=operationRepository.countAllOperationGroups(operationFilter,securityContext);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<OperationGroup> listAllOperationGroups(OperationGroupFilter operationFilter, SecurityContext securityContext) {
		return operationRepository.listAllOperationGroups(operationFilter, securityContext);
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
