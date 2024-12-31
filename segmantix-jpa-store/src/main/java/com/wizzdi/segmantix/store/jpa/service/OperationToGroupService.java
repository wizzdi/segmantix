package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.OperationToGroupRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.model.OperationToGroup;
import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;
import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.OperationToGroupCreate;
import com.wizzdi.segmantix.store.jpa.request.OperationToGroupFilter;
import com.wizzdi.segmantix.store.jpa.request.OperationToGroupUpdate;
import com.wizzdi.segmantix.store.jpa.request.SecurityOperationFilter;
import com.wizzdi.segmantix.store.jpa.response.OperationToGroupContainer;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;



public class OperationToGroupService  implements SegmantixService {

	private final BasicService basicService;
	private final OperationToGroupRepository operationRepository;
	private final SecurityOperationService securityOperationService;

    public OperationToGroupService(BasicService basicService, OperationToGroupRepository operationRepository, SecurityOperationService securityOperationService) {
        this.basicService = basicService;
        this.operationRepository = operationRepository;
        this.securityOperationService = securityOperationService;
    }


    public OperationToGroup createOperationToGroup(OperationToGroupCreate operationToGroupCreate, SecurityContext securityContext){
		OperationToGroup operation= createOperationToGroupNoMerge(operationToGroupCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public OperationToGroup createOperationToGroupNoMerge(OperationToGroupCreate operationToGroupCreate, SecurityContext securityContext){
		OperationToGroup operation=new OperationToGroup();
		operation.setId(UUID.randomUUID().toString());
		updateOperationToGroupNoMerge(operationToGroupCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationToGroupNoMerge(OperationToGroupCreate operationToGroupCreate, OperationToGroup operationToGroup) {
		boolean update = basicService.updateBasicNoMerge(operationToGroupCreate, operationToGroup);
		if(operationToGroupCreate.getOperationGroup()!=null&&(operationToGroup.getOperationGroup()==null||!operationToGroupCreate.getOperationGroup().getId().equals(operationToGroup.getOperationGroup().getId()))){
			operationToGroup.setOperationGroup(operationToGroupCreate.getOperationGroup());
			update=true;
		}
		if(operationToGroupCreate.getOperation()!=null&&!operationToGroupCreate.getOperation().getId().equals(operationToGroup.getOperationId())){
			operationToGroup.setOperationId(operationToGroupCreate.getOperation().getId());
			update=true;
		}
		return update;
	}

	public OperationToGroup updateOperationToGroup(OperationToGroupUpdate operationToGroupUpdate, SecurityContext securityContext){
		OperationToGroup operation=operationToGroupUpdate.getOperationToGroup();
		if(updateOperationToGroupNoMerge(operationToGroupUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public OperationToGroup updateOperationToGroup(OperationToGroupCreate operationToGroupUpdate, OperationToGroup operationToGroup){
		if(updateOperationToGroupNoMerge(operationToGroupUpdate,operationToGroup)){
			operationRepository.merge(operationToGroup);
		}
		return operationToGroup;
	}



	public PaginationResponse<OperationToGroup> getAllOperationToGroups(OperationToGroupFilter operationToGroupFilter, SecurityContext securityContext) {
		List<OperationToGroup> list= listAllOperationToGroups(operationToGroupFilter, securityContext);
		long count=operationRepository.countAllOperationToGroups(operationToGroupFilter,securityContext);
		return new PaginationResponse<>(list,operationToGroupFilter,count);
	}

	public List<OperationToGroup> listAllOperationToGroups(OperationToGroupFilter operationToGroupFilter, SecurityContext securityContext) {
		return operationRepository.listAllOperationToGroups(operationToGroupFilter, securityContext);
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


	public PaginationResponse<OperationToGroupContainer> getAllOperationToGroupsContainers(OperationToGroupFilter operationToGroupFilter, SecurityContext securityContext) {
		PaginationResponse<OperationToGroup> allOperationToGroups = getAllOperationToGroups(operationToGroupFilter, securityContext);
		List<OperationToGroup> list = allOperationToGroups.getList();
		Set<String> usedOperations=list.stream().map(f->f.getOperationId()).collect(Collectors.toSet());
		Map<String,SecurityOperation> map=usedOperations.isEmpty()? Collections.emptyMap():securityOperationService.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(usedOperations))).stream().collect(Collectors.toMap(f->f.getId(), f->f));
		List<OperationToGroupContainer> containers=list.stream().map(f->new OperationToGroupContainer(f.getId(),f.getOperationGroup(),map.get(f.getOperationId()))).toList();
		return new PaginationResponse<>(containers,operationToGroupFilter.getPageSize(), allOperationToGroups.getTotalRecords());
	}

}
