/*
 * Decompiled with CFR 0_100.
 */
package com.greenpepper.eclipse.fixture;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Constructor
implements Comparable<Constructor> {
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

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append(this.getName()).append("(");
        for (int i = 0; i < this.arity; ++i) {
            sb.append("String param").append(i + 1);
            sb.append(i == this.arity - 1 ? "" : ", ");
        }
        sb.append("){");
        sb.append("\n// TODO Auto-generated Constructor stub");
        sb.append("\n}");
        return sb.toString();
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
            return new Integer(this.arity).compareTo(new Integer(other.arity));
        }
        return this.getName().compareTo(other.getName());
    }
}

