package com.wizzdi.segmantix.jpa.store.spring.events;


import com.wizzdi.segmantix.impl.model.Basic;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BasicCreated<E extends Basic>  implements ResolvableTypeProvider {

    private Class<E> type;
    private E baseclass;

    public BasicCreated(E baseclass) {
        this.baseclass = baseclass;
        this.type= (Class<E>) baseclass.getClass();
    }

    public BasicCreated() {
    }

    public E getBaseclass() {
        return baseclass;
    }

    public <T extends BasicCreated<E>> T setBaseclass(E baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    @Override
    public ResolvableType getResolvableType() {
        return type!=null?ResolvableType.forClassWithGenerics(BasicCreated.class,type):ResolvableType.forClass(BasicCreated.class);
    }
}
