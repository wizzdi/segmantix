package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;

@IdValid(targetField = "instanceGroup",field = "id",fieldType = InstanceGroup.class)
public class InstanceGroupDuplicate {

    private String id;

    @JsonIgnore
    private InstanceGroup instanceGroup;

    public String getId() {
        return id;
    }

    public <T extends InstanceGroupDuplicate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    public <T extends InstanceGroupDuplicate> T setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
        return (T) this;
    }
}
