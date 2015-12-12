/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.util.NameUtils
 */
package com.greenpepper.eclipse.fixture;

import com.greenpepper.util.NameUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Method
implements Comparable<Method> {
    private String rawName;
    private int arity;

    public Method(String rawName, int arity) {
        this.rawName = rawName;
        this.arity = arity;
    }

    public String getName() {
        return NameUtils.toLowerCamelCase((String)NameUtils.humanize((String)this.rawName));
    }

    public int getArity() {
        return this.arity;
    }

    public String getRawName() {
        return this.rawName;
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("public String ").append(this.getName()).append("(");
        for (int i = 0; i < this.arity; ++i) {
            sb.append("String param").append(i + 1);
            sb.append(i == this.arity - 1 ? "" : ", ");
        }
        sb.append("){");
        sb.append("\n// TODO Auto-generated Method stub");
        sb.append("\nreturn null;");
        sb.append("\n}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Method)) {
            return false;
        }
        Method other = (Method)o;
        if (this.getName().equals(other.getName()) && this.arity == other.arity) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (String.valueOf(this.getName()) + String.valueOf(this.arity)).hashCode();
    }

    @Override
    public int compareTo(Method other) {
        if (this.getName().equals(other.getName())) {
            return new Integer(this.arity).compareTo(new Integer(other.arity));
        }
        return this.getName().compareTo(other.getName());
    }
}

