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
public class Property
implements Comparable<Property> {
    private String rawName;

    public Property(String rawName) {
        this.rawName = rawName;
    }

    public String getName() {
        return NameUtils.toLowerCamelCase((String)NameUtils.humanize((String)this.rawName));
    }

    public String getRawName() {
        return this.rawName;
    }

    public String toCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("public String ").append(this.getName()).append(";");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Property)) {
            return false;
        }
        return this.getName().equals(((Property)o).getName());
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public int compareTo(Property other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }
}

