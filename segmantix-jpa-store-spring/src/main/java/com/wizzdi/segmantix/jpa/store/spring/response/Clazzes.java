package com.wizzdi.segmantix.jpa.store.spring.response;

import com.flexicore.model.Clazz;

import java.util.List;

public class Clazzes {

	private final List<Clazz> clazzes;

	public Clazzes(List<Clazz> clazzes) {
		this.clazzes = clazzes;
	}

	public List<Clazz> getClazzes() {
		return clazzes;
	}
}
