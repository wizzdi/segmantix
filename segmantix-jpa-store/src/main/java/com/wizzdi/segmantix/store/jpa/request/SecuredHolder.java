package com.wizzdi.segmantix.store.jpa.request;

import com.wizzdi.segmantix.store.jpa.model.Clazz;

public record SecuredHolder(String id, Clazz type) {
}
