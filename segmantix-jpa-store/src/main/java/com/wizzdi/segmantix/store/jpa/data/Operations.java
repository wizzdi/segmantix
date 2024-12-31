package com.wizzdi.segmantix.store.jpa.data;

import com.wizzdi.segmantix.store.jpa.model.SecurityOperation;

import java.util.List;

public record Operations(List<SecurityOperation> operations) {
}
