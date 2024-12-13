package com.wizzdi.segmantix.jpa.store.spring.service;

import com.flexicore.model.Basic;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.segmantix.jpa.store.spring.request.BasicCreate;
import com.wizzdi.segmantix.jpa.store.spring.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.jpa.store.spring.request.PaginationFilter;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component

public class BasicService  {



	public boolean updateBasicNoMerge(BasicCreate basicCreate, Basic basic) {
		boolean update = false;
		if(basicCreate.getUnsetProperties()!=null&&!basicCreate.getUnsetProperties().isEmpty()){
			BeanWrapperImpl objectWrapper = new BeanWrapperImpl(basic);
			for (String propertyName : basicCreate.getUnsetProperties()) {
				Object currentValue = objectWrapper.getPropertyValue(propertyName);
				if(currentValue!=null&&!currentValue.getClass().isPrimitive()){
					objectWrapper.setPropertyValue(propertyName,null);
					update=true;
				}
			}
		}
		if(basicCreate.getIdForCreate()!=null&&(basic.getId()==null||!basicCreate.getIdForCreate().equals(basic.getId()))){
			basic.setId(basicCreate.getIdForCreate());
			update=true;
		}

		if (basicCreate.getUpdateDate() != null && (!basicCreate.getUpdateDate().equals(basic.getUpdateDate()))) {
			basic.setUpdateDate(basicCreate.getUpdateDate());
			update = true;
		}

		if (basicCreate.getName() != null && (!basicCreate.getName().equals(basic.getName()))) {
			basic.setName(basicCreate.getName());
			update = true;
		}

		if (basicCreate.getDescription() != null && (!basicCreate.getDescription().equals(basic.getDescription()))) {
			basic.setDescription(basicCreate.getDescription());
			update = true;
		}
		if (basicCreate.getSoftDelete() != null && (!basicCreate.getSoftDelete().equals(basic.isSoftDelete()))) {
			basic.setSoftDelete(basicCreate.getSoftDelete());
			update = true;
		}

		return update;
	}

	@Deprecated
	public void validate(BasicCreate basicCreate, SecurityContext securityContext) {

	}

	@Deprecated
	public void validate(PaginationFilter paginationFilter, SecurityContext securityContext) {

	}

	@Deprecated
	public void validate(BasicPropertiesFilter basicPropertiesFilter, SecurityContext securityContext) {

	}


}
