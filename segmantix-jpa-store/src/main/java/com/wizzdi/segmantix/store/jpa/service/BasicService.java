package com.wizzdi.segmantix.store.jpa.service;

import com.wizzdi.segmantix.store.jpa.interfaces.SegmantixService;
import com.wizzdi.segmantix.store.jpa.model.Basic;
import com.wizzdi.segmantix.store.jpa.request.BasicCreate;


public class BasicService  implements SegmantixService {



	public boolean updateBasicNoMerge(BasicCreate basicCreate, Basic basic) {
		boolean update = false;

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



}
