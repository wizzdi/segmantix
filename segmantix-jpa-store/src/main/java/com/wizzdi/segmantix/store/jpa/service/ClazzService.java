package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.ClazzRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Clazz;
import com.wizzdi.segmantix.store.jpa.model.SecurityWildcard;
import com.wizzdi.segmantix.store.jpa.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.store.jpa.request.ClazzFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;



public class ClazzService  implements SegmantixService {

	private final ClazzRepository clazzRepository;

	public ClazzService(ClazzRepository clazzRepository) {
		this.clazzRepository = clazzRepository;
	}

	public PaginationResponse<Clazz> getAllClazzs(ClazzFilter clazzFilter) {
		List<Clazz> list= listAllClazzs(clazzFilter );
		long count= clazzRepository.countAllClazzs(clazzFilter);
		return new PaginationResponse<>(list,clazzFilter,count);
	}

	public List<Clazz> listAllClazzs(ClazzFilter clazzFilter) {
		return clazzRepository.listAllClazzs(clazzFilter );
	}


	public Optional<Clazz> getClazz(Class<?> aClass) {
		return clazzRepository.listAllClazzs(new ClazzFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(Set.of(aClass.getSimpleName())))).stream().findFirst();
	}

	public void addClazz(Clazz clazz){
		clazzRepository.add(clazz);
	}
	public Clazz getWildcardClazz(){
		return getClazz(SecurityWildcard.class).orElse(null);
	}
}
