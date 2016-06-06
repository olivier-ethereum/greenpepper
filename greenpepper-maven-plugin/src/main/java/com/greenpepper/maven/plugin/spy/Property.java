package com.greenpepper.maven.plugin.spy;

import com.greenpepper.util.NameUtils;

public class Property extends Spy<Property> {

    public Property(String rawName) {
        super(rawName);
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

}

