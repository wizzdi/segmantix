package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.data.BaseclassRepository;
import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Baseclass;
import com.wizzdi.segmantix.store.jpa.request.BaseclassCreate;
import com.wizzdi.segmantix.store.jpa.request.BaseclassFilter;
import com.wizzdi.segmantix.store.jpa.response.PaginationResponse;

import java.util.List;



public class BaseclassService implements SegmantixService {

	private final BasicService basicService;
	private final BaseclassRepository baseclassRepository;

	public BaseclassService(BasicService basicService, BaseclassRepository baseclassRepository) {
		this.basicService = basicService;
		this.baseclassRepository = baseclassRepository;
	}





	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = basicService.updateBasicNoMerge(baseclassCreate,baseclass);


		return update;
	}

	public PaginationResponse<?> getAllBaseclass(BaseclassFilter baseclasssFilter, SecurityContext securityContext) {
		List<?> baseclasses = listAllBaseclass(baseclasssFilter, securityContext);
		long count = countAllBaseclass(baseclasssFilter, securityContext);
		return new PaginationResponse<>(baseclasses, baseclasssFilter, count);
	}

	private long countAllBaseclass(BaseclassFilter baseclasssFilter, SecurityContext securityContext) {
		if(baseclasssFilter.getClazzes()==null||baseclasssFilter.getClazzes().size()!=1){
			throw new IllegalArgumentException("listing baseclass generically only supports exactly one clazz to be provided");
		}
		String typeSimpleName = baseclasssFilter.getClazzes().getFirst().name();
		Class<?> type = baseclassRepository.getEntities().stream().map(f -> f.getJavaType()).filter(f -> f.getSimpleName().equals(typeSimpleName)).findFirst().orElseThrow(() -> new IllegalArgumentException( "no entity of type %s".formatted(typeSimpleName)));
		if(Baseclass.class.isAssignableFrom(type)){
			return baseclassRepository.countAllBaseclass((Class<? extends Baseclass>) type, baseclasssFilter, securityContext);
		}
		return baseclassRepository.countAll(type,baseclasssFilter,securityContext);

	}

	public List<?> listAllBaseclass(BaseclassFilter baseclasssFilter, SecurityContext securityContext) {
		if(baseclasssFilter.getClazzes()==null||baseclasssFilter.getClazzes().size()!=1){
			throw new IllegalArgumentException("listing baseclass generically only supports exactly one clazz to be provided");
		}
		String typeSimpleName = baseclasssFilter.getClazzes().getFirst().name();
		Class<?> type = baseclassRepository.getEntities().stream().map(f -> f.getJavaType()).filter(f -> f.getSimpleName().equals(typeSimpleName)).findFirst().orElseThrow(() -> new IllegalArgumentException( "no entity of type %s".formatted(typeSimpleName)));
		if(Baseclass.class.isAssignableFrom(type)){
			return baseclassRepository.listAllBaseclass((Class<? extends Baseclass>) type, baseclasssFilter, securityContext);
		}
		return baseclassRepository.listAll(type,baseclasssFilter,securityContext);

	}

	public static <T extends Baseclass> Baseclass createSecurityObjectNoMerge(T subject, SecurityContext securityContext) {
		subject.setSecurityId(subject.getId());
		if(securityContext==null){
			return subject;
		}
		subject.setCreator(securityContext.getUser());
		subject.setTenant(securityContext.getTenantToCreateIn());
		//TODO:clazz?
		return subject;
	}



	public void createIndexes(boolean recreateIndexes) {
		baseclassRepository.createIndexes(recreateIndexes);
	}
}
