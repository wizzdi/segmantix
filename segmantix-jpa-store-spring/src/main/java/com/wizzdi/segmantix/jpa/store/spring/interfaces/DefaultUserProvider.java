package com.wizzdi.segmantix.jpa.store.spring.interfaces;

import com.wizzdi.segmantix.impl.model.User;
import com.wizzdi.segmantix.jpa.store.spring.request.UserCreate;

public interface DefaultUserProvider<T extends User> {

    T createUser(UserCreate userCreate);

}
