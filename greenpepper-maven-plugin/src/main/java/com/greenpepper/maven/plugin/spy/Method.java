package com.greenpepper.maven.plugin.spy;

import com.greenpepper.util.NameUtils;

public class Method implements Comparable<Method> {
    private String rawName;
    private int arity;

    public Method(String rawName, int arity) {
        this.rawName = rawName;
        this.arity = arity;
    }

    public String getName() {
        return NameUtils.toLowerCamelCase(NameUtils.humanize(this.rawName));
    }

    public int getArity() {
        return this.arity;
    }

    public String getRawName() {
        return this.rawName;
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
}

