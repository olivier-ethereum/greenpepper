package com.greenpepper.maven.plugin.spy;

import com.greenpepper.reflect.Message;

public class PojoSpyFixture extends SpyFixture {

    public PojoSpyFixture(String fixtureName) {
        super(fixtureName);
    }

    @Override
    public Message check(String message) {
        return SpyOn.property(this, message);
    }

    public Pojo getPojo() {
        return new Pojo(getName() + " item", getProperties());
    }
}
