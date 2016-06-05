package com.greenpepper.maven.plugin.spy;

import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.EnterRow;
import com.greenpepper.reflect.Message;

import java.util.Collection;
import java.util.Collections;

public class SpySubFixture extends SpyFixture {

    public enum SubFixtureType {
        COLLECTION_PROVIDER, SETUP;
    }

    private SubFixtureType type;

    public SpySubFixture(String fixtureName) {
        super(fixtureName);
    }

    public SubFixtureType getType() {
        return type;
    }

    public void setType(SubFixtureType type) {
        this.type = type;
    }

    @Override
    public Message check(String message) {
        return SpyOn.property(this, message);
    }

    public Pojo getPojo() {
        return new Pojo(getName() + " item", getProperties());
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
}
