package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.OperationGroupLink;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.OperationGroupLinkRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationGroupLinkUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class OperationGroupLinkService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private OperationGroupLinkRepository operationRepository;


	public OperationGroupLink createOperationGroupLink(OperationGroupLinkCreate operationGroupLinkCreate, SecurityContext securityContext){
		OperationGroupLink operation= createOperationGroupLinkNoMerge(operationGroupLinkCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public OperationGroupLink createOperationGroupLinkNoMerge(OperationGroupLinkCreate operationGroupLinkCreate, SecurityContext securityContext){
		OperationGroupLink operation=new OperationGroupLink();
		operation.setId(UUID.randomUUID().toString());
		updateOperationGroupLinkNoMerge(operationGroupLinkCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationGroupLinkNoMerge(OperationGroupLinkCreate operationGroupLinkCreate, OperationGroupLink operationGroupLink) {
		boolean update = basicService.updateBasicNoMerge(operationGroupLinkCreate, operationGroupLink);
		if(operationGroupLinkCreate.getOperationGroup()!=null&&(operationGroupLink.getOperationGroup()==null||!operationGroupLinkCreate.getOperationGroup().getId().equals(operationGroupLink.getOperationGroup().getId()))){
			operationGroupLink.setOperationGroup(operationGroupLinkCreate.getOperationGroup());
			update=true;
		}
		if(operationGroupLinkCreate.getOperation()!=null&&(operationGroupLink.getOperation()==null||!operationGroupLinkCreate.getOperation().getId().equals(operationGroupLink.getOperation().getId()))){
			operationGroupLink.setOperation(operationGroupLinkCreate.getOperation());
			update=true;
		}
		return update;
	}

	public OperationGroupLink updateOperationGroupLink(OperationGroupLinkUpdate operationGroupLinkUpdate, SecurityContext securityContext){
		OperationGroupLink operation=operationGroupLinkUpdate.getOperationGroupLink();
		if(updateOperationGroupLinkNoMerge(operationGroupLinkUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public OperationGroupLink updateOperationGroupLink(OperationGroupLinkCreate operationGroupLinkUpdate, OperationGroupLink operationGroupLink){
		if(updateOperationGroupLinkNoMerge(operationGroupLinkUpdate,operationGroupLink)){
			operationRepository.merge(operationGroupLink);
		}
		return operationGroupLink;
	}



	public PaginationResponse<OperationGroupLink> getAllOperationGroupLinks(OperationGroupLinkFilter operationGroupLinkFilter, SecurityContext securityContext) {
		List<OperationGroupLink> list= listAllOperationGroupLinks(operationGroupLinkFilter, securityContext);
		long count=operationRepository.countAllOperationGroupLinks(operationGroupLinkFilter,securityContext);
		return new PaginationResponse<>(list,operationGroupLinkFilter,count);
	}

	public List<OperationGroupLink> listAllOperationGroupLinks(OperationGroupLinkFilter operationGroupLinkFilter, SecurityContext securityContext) {
		return operationRepository.listAllOperationGroupLinks(operationGroupLinkFilter, securityContext);
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
