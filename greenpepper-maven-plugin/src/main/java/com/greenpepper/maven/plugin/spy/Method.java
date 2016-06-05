package com.greenpepper.maven.plugin.spy;

public class Method extends Spy<Method> {

    private int arity;
    private SpyFixture subFixtureSpy;

    public Method(String rawName, int arity) {
        super(rawName);
        this.arity = arity;
    }

    public int getArity() {
        return this.arity;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Method)) {
            return false;
        }
        Method other = (Method)o;
        return this.getName().equals(other.getName()) && this.arity == other.arity;
    }

    public int hashCode() {
        return (String.valueOf(this.getName()) + String.valueOf(this.arity)).hashCode();
    }

    @Override
    public int compareTo(Method other) {
        if (this.getName().equals(other.getName())) {
            return new Integer(this.arity).compareTo(other.arity);
        }
        return this.getName().compareTo(other.getName());
    }

    public void setSubFixtureSpy(SpyFixture subFixtureSpy) {
        this.subFixtureSpy = subFixtureSpy;
    }

    public SpyFixture getSubFixtureSpy() {
        return subFixtureSpy;
    }
}

