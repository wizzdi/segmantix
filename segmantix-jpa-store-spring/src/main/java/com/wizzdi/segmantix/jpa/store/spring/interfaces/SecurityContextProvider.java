package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.impl.model.Operation;
import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.model.SecurityContext;

public interface SecurityContextProvider {

    SecurityContext getSecurityContext(User user, Operation operation);
}
