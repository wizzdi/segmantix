package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "instanceGroup", fieldType = InstanceGroup.class, field = "id", groups = {Update.class})
})
public class InstanceGroupUpdate extends InstanceGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private InstanceGroup instanceGroup;

    public String getId() {
        return id;
    }

    public <T extends InstanceGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    public <T extends InstanceGroupUpdate> T setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
        return (T) this;
    }
}
