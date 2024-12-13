package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wizzdi.segmantix.impl.model.Baseclass;
import com.wizzdi.segmantix.impl.model.InstanceGroup;
import com.wizzdi.segmantix.jpa.store.spring.validation.Create;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "baseclasses", fieldType = Baseclass.class, field = "baseclassesIds", groups = {Create.class, Update.class}),
        @IdValid(targetField = "instanceGroups", fieldType = InstanceGroup.class, field = "instanceGroupIds", groups = {Create.class, Update.class})


})
public class InstanceGroupLinkMassCreate {

    private Set<String> baseclassesIds = new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclasses;
    private Set<String> instanceGroupIds = new HashSet<>();
    @JsonIgnore
    private List<InstanceGroup> instanceGroups;

    public Set<String> getBaseclassesIds() {
        return baseclassesIds;
    }

    public <T extends InstanceGroupLinkMassCreate> T setBaseclassesIds(Set<String> baseclassesIds) {
        this.baseclassesIds = baseclassesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends InstanceGroupLinkMassCreate> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public Set<String> getInstanceGroupIds() {
        return instanceGroupIds;
    }

    public <T extends InstanceGroupLinkMassCreate> T setInstanceGroupIds(Set<String> instanceGroupIds) {
        this.instanceGroupIds = instanceGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<InstanceGroup> getInstanceGroups() {
        return instanceGroups;
    }

    public <T extends InstanceGroupLinkMassCreate> T setInstanceGroups(List<InstanceGroup> instanceGroups) {
        this.instanceGroups = instanceGroups;
        return (T) this;
    }
}
