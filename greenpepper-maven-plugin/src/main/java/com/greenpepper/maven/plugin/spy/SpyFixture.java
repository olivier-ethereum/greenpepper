package com.greenpepper.maven.plugin.spy;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SpyFixture implements Fixture {
    private SortedSet<Constructor> constructors = new TreeSet<Constructor>();
    private SortedSet<Property> properties = new TreeSet<Property>();
    private SortedSet<Method> methods = new TreeSet<Method>();
    private String name;

    public SpyFixture(String fixtureName) {
        this.name = fixtureName;
    }

    public boolean canCheck(String message) {
        return true;
    }

    public Message check(String message) {
        return SpyOn.function(this, message);
    }

    public Message getter(String message) {
        return SpyOn.function(this, message);
    }

    public boolean respondsTo(String message) {
        return true;
    }

    public boolean canSend(String message) {
        return true;
    }

    public Message send(String message) {
        return SpyOn.property(this, message);
    }

    public Fixture fixtureFor(Object target) {
        return this;
    }

    public Object getTarget() {
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Set<Property> getProperties() {
        return this.properties;
    }

    public Set<Method> getMethods() {
        return this.methods;
    }

    public Set<Constructor> getConstructors() {
        return this.constructors;
    }

    public void addConstructors(Constructor constructor) {
        this.constructors.add(constructor);
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof SpyFixture)) {
            return false;
        }
        return this.name.equals(((SpyFixture)other).name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}

