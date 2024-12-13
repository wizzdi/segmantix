package com.wizzdi.segmantix.jpa.store.spring.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.impl.model.InstanceGroupLink;
import com.wizzdi.segmantix.jpa.store.spring.validation.IdValid;
import com.wizzdi.segmantix.jpa.store.spring.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "instanceGroupLink", fieldType = InstanceGroupLink.class, field = "id", groups = {Update.class})
})
public class InstanceGroupLinkUpdate extends InstanceGroupLinkCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private InstanceGroupLink instanceGroupLink;

    public String getId() {
        return id;
    }

    public <T extends InstanceGroupLinkUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public InstanceGroupLink getInstanceGroupLink() {
        return instanceGroupLink;
    }

    public <T extends InstanceGroupLinkUpdate> T setInstanceGroupLink(InstanceGroupLink instanceGroupLink) {
        this.instanceGroupLink = instanceGroupLink;
        return (T) this;
    }
}
