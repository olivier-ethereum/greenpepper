package com.greenpepper.maven.plugin.spy;

import com.greenpepper.util.NameUtils;

public class Property implements Comparable<Property> {
    private String rawName;

    public Property(String rawName) {
        this.rawName = rawName;
    }

    public String getName() {
        return NameUtils.toLowerCamelCase(NameUtils.humanize(this.rawName));
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

