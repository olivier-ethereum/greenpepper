package com.greenpepper.maven.plugin.spy;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class Pojo extends Spy<Pojo> {

    private final Collection<Property> properties;

    public Pojo(String rawName, Collection<Property> properties) {
        super(rawName);
        this.properties = properties;
    }

    public Collection<Property> getProperties() {
        return properties;
    }

    @Override
    public String getName() {
        return StringUtils.capitalize(super.getName());
    }
}
