package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.OperationToClazz;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.data.OperationToClazzRepository;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.OperationToClazzUpdate;
import com.wizzdi.segmantix.jpa.store.spring.response.PaginationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Component
public class OperationToClazzService  {

	@Autowired
	private BasicService basicService;
	@Autowired
	private OperationToClazzRepository operationToClazzRepository;


	public OperationToClazz createOperationToClazz(OperationToClazzCreate operationToClazzCreate, SecurityContext securityContext){
		OperationToClazz operationToClazz= createOperationToClazzNoMerge(operationToClazzCreate,securityContext);
		operationToClazzRepository.merge(operationToClazz);
		return operationToClazz;
	}
	public <T> T merge(T o){
		return operationToClazzRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		operationToClazzRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContext securityContext) {
		return operationToClazzRepository.listByIds(c, ids, securityContext);
	}

	public OperationToClazz createOperationToClazzNoMerge(OperationToClazzCreate operationToClazzCreate, SecurityContext securityContext){
		OperationToClazz operationToClazz=new OperationToClazz();
		operationToClazz.setId(UUID.randomUUID().toString());
		updateOperationToClazzNoMerge(operationToClazzCreate,operationToClazz);
		return operationToClazz;
	}

	public boolean updateOperationToClazzNoMerge(OperationToClazzCreate operationToClazzCreate, OperationToClazz operationToClazz) {
		boolean update= basicService.updateBasicNoMerge(operationToClazzCreate,operationToClazz);
		if(operationToClazzCreate.getClazz()!=null&&(operationToClazz.getClazz()==null||!operationToClazzCreate.getClazz().getId().equals(operationToClazz.getClazz().getId()))){
			operationToClazz.setClazz(operationToClazzCreate.getClazz());
			update=true;
		}
		if(operationToClazzCreate.getOperation()!=null&&(operationToClazz.getOperation()==null||!operationToClazzCreate.getOperation().getId().equals(operationToClazz.getOperation().getId()))){
			operationToClazz.setOperation(operationToClazzCreate.getOperation());
			update=true;
		}
		return update;
	}

	public OperationToClazz updateOperationToClazz(OperationToClazzUpdate operationToClazzUpdate, SecurityContext securityContext){
		OperationToClazz operationToClazz=operationToClazzUpdate.getOperationToClazz();
		if(updateOperationToClazzNoMerge(operationToClazzUpdate,operationToClazz)){
			operationToClazzRepository.merge(operationToClazz);
		}
		return operationToClazz;
	}

	@Deprecated
	public void validate(OperationToClazzCreate operationToClazzCreate, SecurityContext securityContext) {
		basicService.validate(operationToClazzCreate,securityContext);
	}

	@Deprecated
	public void validate(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext) {
		basicService.validate(operationToClazzFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return operationToClazzRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<OperationToClazz> getAllOperationToClazz(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext) {
		List<OperationToClazz> list= listAllOperationToClazz(operationToClazzFilter, securityContext);
		long count=operationToClazzRepository.countAllOperationToClazzs(operationToClazzFilter,securityContext);
		return new PaginationResponse<>(list,operationToClazzFilter,count);
	}

	public List<OperationToClazz> listAllOperationToClazz(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext) {
		return operationToClazzRepository.listAllOperationToClazzs(operationToClazzFilter, securityContext);
	}
}
