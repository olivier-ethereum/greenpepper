package com.greenpepper.maven.plugin.spy;

import com.greenpepper.util.NameUtils;

public abstract class Spy<T extends Spy> implements Comparable<T> {

    private String rawName;

    public Spy(String rawName) {
        this.rawName = rawName;
    }

    public String getName() {
        return NameUtils.toLowerCamelCase(NameUtils.humanize(this.rawName));
    }

    public String getRawName() {
        return this.rawName;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public int compareTo(T other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }
}
