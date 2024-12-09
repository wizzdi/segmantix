package com.wizzdi.segmantix.app;


public class TestEntityCreate  {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public <T extends TestEntityCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends TestEntityCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }
}
