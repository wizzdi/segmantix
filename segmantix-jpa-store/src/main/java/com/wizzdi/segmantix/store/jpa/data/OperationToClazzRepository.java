package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.store.jpa.request.OperationToClazzFilter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;



public class OperationToClazzRepository  implements SegmantixRepository {

	private final Map<String,OperationToClazz> operationToClazzMap=new ConcurrentHashMap<>();

	public List<OperationToClazz> listAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter ) {
		return streamOperationToClazz(operationToClazzFilter).sorted(Comparator.comparing((OperationToClazz f)->f.operation().name()).thenComparing((OperationToClazz f)->f.clazz().name())).toList();

	}

	private Stream<OperationToClazz> streamOperationToClazz(OperationToClazzFilter operationToClazzFilter) {
		return operationToClazzMap.values().stream().filter(f->filter(f,operationToClazzFilter));
	}

	private boolean filter(OperationToClazz operationToClazz, OperationToClazzFilter operationToClazzFilter) {

		boolean pass=true;
		List<Clazz> clazzes = operationToClazzFilter.getClazzes();
		if(clazzes !=null&&!clazzes.isEmpty()){
			pass=pass&&clazzes.contains(operationToClazz.clazz());
		}
		List<SecurityOperation> securityOperations = operationToClazzFilter.getSecurityOperations();
		if(securityOperations!=null&&!securityOperations.isEmpty()){
			pass=pass&&securityOperations.contains(operationToClazz.operation());
		}

		return pass;
	}




	public long countAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter) {
		return streamOperationToClazz(operationToClazzFilter).count();

	}

	public void addOperationToClazz(OperationToClazz operationToClazz) {

		operationToClazzMap.put(operationToClazz.clazz().name()+"_"+operationToClazz.operation().getId(),operationToClazz);
	}
}
