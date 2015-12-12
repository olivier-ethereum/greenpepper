/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.reflect.Message
 */
package com.greenpepper.eclipse.fixture;

import com.greenpepper.eclipse.fixture.Method;
import com.greenpepper.eclipse.fixture.Property;
import com.greenpepper.eclipse.fixture.SpyFixture;
import com.greenpepper.reflect.Message;

class SpyOn
extends Message {
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

    public /* varargs */ Object send(String ... args) {
        this.arity = args.length;
        if (this.forProperty) {
            this.spyFixture.addProperty(new Property(this.message));
        } else {
            this.spyFixture.addMethod(new Method(this.message, this.arity));
        }
        return new Object();
    }

    public int getArity() {
        return this.arity;
    }
}

