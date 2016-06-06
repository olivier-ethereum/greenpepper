package com.greenpepper.maven.plugin.spy;

import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.EnterRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.greenpepper.maven.plugin.spy.SpyFixture.SubFixtureType.WORKFLOW;

public class SpyFixture implements Fixture {
    private SortedSet<Constructor> constructors = new TreeSet<Constructor>();
    private SortedSet<Property> properties = new TreeSet<Property>();
    private SortedSet<Method> methods = new TreeSet<Method>();
    private String name;

    private String rawName;
    private SubFixtureType type = WORKFLOW;

    public SpyFixture(String fixtureName) {
        this.name = fixtureName;
    }

    public boolean canCheck(String message) {
        return true;
    }

    public SubFixtureType getType() {
        return type;
    }

    public void setType(SubFixtureType type) {
        this.type = type;
    }

    public Message check(String message) {
        SpyOn spyOn;
        switch (type) {
            case COLLECTION_PROVIDER:
                spyOn = SpyOn.property(this, message);
                break;
            case SETUP:
            default:
                spyOn = SpyOn.function(this, message);
        }
        return spyOn;
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
        if (target instanceof SpyCallResult) {
            SpyCallResult spyCallResult = (SpyCallResult) target;
            SpyFixture spyFixture = null;
            for (Method method : methods) {
                if (method.getArity() == 0 && StringUtils.equals(method.getRawName(),spyCallResult.message) ) {
                    spyFixture =  method.getSubFixtureSpy();
                    if (spyFixture != null) {
                        break;
                    } else {
                        spyFixture = new SpyFixture(spyCallResult.message);
                        spyFixture.rawName = spyCallResult.message;
                        method.setSubFixtureSpy(spyFixture);
                        break;
                    }
                }
            }
            if (spyFixture == null) {
                throw new IllegalArgumentException("The method for the collection provider should have already been seen.");
            }
            return spyFixture;
        }
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

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getRawName() {
        return rawName;
    }

    @CollectionProvider
    public Collection<?> spyForCollectionProvider() {
        type = SubFixtureType.COLLECTION_PROVIDER;
        return Collections.singleton(this);
    }

    @EnterRow
    public void spyForEnterRow() {
        type = SubFixtureType.SETUP;
    }

    public Pojo getPojo() {
        return new Pojo(getRawName() + " item", getProperties());
    }

    public enum SubFixtureType {
        COLLECTION_PROVIDER, SETUP, WORKFLOW;
    }
}

