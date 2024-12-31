package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.OperationToClazzRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.OperationToClazz;
import com.wizzdi.segmantix.store.jpa.request.OperationToClazzCreate;
import com.wizzdi.segmantix.store.jpa.request.OperationToClazzFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;



public class OperationToClazzService implements SegmantixService {


	private OperationToClazzRepository operationToClazzRepository;


	public OperationToClazz addOperationToClazz(OperationToClazzCreate operationToClazzCreate){
		OperationToClazz operationToClazz=new OperationToClazz(operationToClazzCreate.getSecurityOperation(),operationToClazzCreate.getType());
		operationToClazzRepository.addOperationToClazz(operationToClazz);
		return operationToClazz;
	}



	public PaginationResponse<OperationToClazz> getAllOperationToClazz(OperationToClazzFilter operationToClazzFilter ) {
		List<OperationToClazz> list= listAllOperationToClazz(operationToClazzFilter);
		long count=operationToClazzRepository.countAllOperationToClazzs(operationToClazzFilter);
		return new PaginationResponse<>(list,operationToClazzFilter,count);
	}

	public List<OperationToClazz> listAllOperationToClazz(OperationToClazzFilter operationToClazzFilter) {
		return operationToClazzRepository.listAllOperationToClazzs(operationToClazzFilter );
	}
}
