package com.wizzdi.segmantix.app;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestEntity  {
    @Id
    private String id;
    private String creatorId;
    private String tenantId;
    private String name;
    private String description;

    public String getId() {
        return id;
    }

    public <T extends TestEntity> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public <T extends TestEntity> T setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends TestEntity> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends TestEntity> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends TestEntity> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

}
