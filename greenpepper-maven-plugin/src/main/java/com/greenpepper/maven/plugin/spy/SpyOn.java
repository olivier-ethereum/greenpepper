package com.greenpepper.maven.plugin.spy;

import com.greenpepper.reflect.Message;

class SpyOn extends Message {
    private int arity;
    private SpyFixture spyFixture;
    private String message;
    private boolean forProperty;

    public SpyOn(SpyFixture spyFixture, String message, boolean forProperty) {
        this.spyFixture = spyFixture;
        this.message = message;
        this.forProperty = forProperty;
    }

    public static SpyOn function(SpyFixture spyFixture, String message) {
        return new SpyOn(spyFixture, message, false);
    }

    public static SpyOn property(SpyFixture spyFixture, String message) {
        return new SpyOn(spyFixture, message, true);
    }

    public Object send(String ... args) {
        this.arity = args.length;
        if (this.forProperty) {
            this.spyFixture.addProperty(new Property(this.message));
        } else {
            this.spyFixture.addMethod(new Method(this.message, this.arity));
        }
        return new SpyCallResult(this.message);
    }

    public int getArity() {
        return this.arity;
    }

}

