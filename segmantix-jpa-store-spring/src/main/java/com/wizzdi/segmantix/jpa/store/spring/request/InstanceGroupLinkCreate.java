package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

@IdValid.List({
        @IdValid(targetField = "instanceGroup", fieldType = InstanceGroup.class, field = "instanceGroupId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "baseclass", fieldType = Baseclass.class, field = "baseclassId", groups = {Create.class, Update.class})


})
public class InstanceGroupLinkCreate extends BasicCreate {

    @JsonIgnore
    private InstanceGroup instanceGroup;
    private String instanceGroupId;
    @JsonIgnore
    private Baseclass baseclass;
    private String baseclassId;

    @JsonIgnore
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    public <T extends InstanceGroupLinkCreate> T setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
        return (T) this;
    }

    public String getInstanceGroupId() {
        return instanceGroupId;
    }

    public <T extends InstanceGroupLinkCreate> T setInstanceGroupId(String instanceGroupId) {
        this.instanceGroupId = instanceGroupId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getBaseclass() {
        return baseclass;
    }

    public <T extends InstanceGroupLinkCreate> T setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    public String getBaseclassId() {
        return baseclassId;
    }

    public <T extends InstanceGroupLinkCreate> T setBaseclassId(String baseclassId) {
        this.baseclassId = baseclassId;
        return (T) this;
    }
}
