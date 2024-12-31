package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixRepository;
import com.wizzdi.segmantix.store.jpa.model.*;
import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.SecurityOperationFilter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class SecurityOperationRepository  implements SegmantixRepository {

	private final Map<String,SecurityOperation> securityOperations=new ConcurrentHashMap<>();

	public SecurityOperationRepository(Operations operations) {
		for (SecurityOperation operation : operations.operations()) {
			securityOperations.put(operation.id(),operation);
		}
	}

	public List<SecurityOperation> listAllOperations(SecurityOperationFilter securityOperationFilter ) {
		Stream<SecurityOperation> stream = streamSecurityOperation(securityOperationFilter).sorted(Comparator.comparing(f -> f.name()));
		if(securityOperationFilter.getPageSize()!=null&&securityOperationFilter.getCurrentPage()!=null&&securityOperationFilter.getCurrentPage()>-1&&securityOperationFilter.getPageSize()>0){
			stream=stream.skip((long) securityOperationFilter.getPageSize() *securityOperationFilter.getCurrentPage()).limit(securityOperationFilter.getPageSize());
		}
		return stream.collect(Collectors.toList());

	}

	private Stream<SecurityOperation> streamSecurityOperation(SecurityOperationFilter securityOperationFilter) {
		return securityOperations.values().stream().filter(f-> filterBasic(f,securityOperationFilter.getBasicPropertiesFilter()));
	}

	private boolean filterBasic(SecurityOperation securityOperation, BasicPropertiesFilter basicPropertiesFilter) {
		if(basicPropertiesFilter==null){
			return true;
		}
		boolean pass=true;
		if(basicPropertiesFilter.getNameLike()!=null){
			String like = basicPropertiesFilter.getNameLike().replace("%", "");
			if(basicPropertiesFilter.isNameLikeCaseSensitive()){
				pass=pass&&securityOperation.name().contains(like);
			}
			else{

				pass=pass&&securityOperation.name().toLowerCase().contains(like.toLowerCase());
			}
		}
		if(basicPropertiesFilter.getOnlyIds()!=null&&!basicPropertiesFilter.getOnlyIds().isEmpty()){
			pass=pass&&basicPropertiesFilter.getOnlyIds().contains(securityOperation.id());
		}

		if(basicPropertiesFilter.getNames()!=null&&!basicPropertiesFilter.getNames().isEmpty()){
			pass=pass&&basicPropertiesFilter.getNames().contains(securityOperation.name());
		}
		return pass;

	}



	public long countAllOperations(SecurityOperationFilter securityOperationFilter) {
		return streamSecurityOperation(securityOperationFilter).count();

	}

	public SecurityOperation addOperation(SecurityOperation securityOperation) {
		securityOperations.put(securityOperation.id(),securityOperation);
		return securityOperation;
	}
}
