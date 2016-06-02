package com.greenpepper.maven.plugin.spy;

public class Constructor implements Comparable<Constructor> {
    private String name;
    private int arity;

    public Constructor(String name, int arity) {
        this.name = name;
        this.arity = arity;
    }

    public String getName() {
        return this.name;
    }

    public int getArity() {
        return this.arity;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Constructor)) {
            return false;
        }
        Constructor other = (Constructor)o;
        if (this.getName().equals(other.getName()) && this.arity == other.arity) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (String.valueOf(this.getName()) + String.valueOf(this.arity)).hashCode();
    }

    @Override
    public int compareTo(Constructor other) {
        if (this.getName().equals(other.getName())) {
            return new Integer(this.arity).compareTo(other.arity);
        }
        return this.getName().compareTo(other.getName());
    }
}

