/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.reflect.Fixture
 *  com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment
 *  com.greenpepper.util.NameUtils
 *  com.greenpepper.util.StringUtil
 */
package com.greenpepper.eclipse.fixture;

import com.greenpepper.eclipse.fixture.Constructor;
import com.greenpepper.eclipse.fixture.SpyFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.NameUtils;
import com.greenpepper.util.StringUtil;
import java.util.HashMap;
import java.util.HashSet;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 * Failed to analyse overrides
 */
public class SpySystemUnderDevelopment
extends DefaultSystemUnderDevelopment {
    private HashMap<String, SpyFixture> fixtures = new HashMap();
    private HashSet<String> imports = new HashSet();

    public HashMap<String, SpyFixture> getFixtures() {
        return this.fixtures;
    }

    public HashSet<String> getImports() {
        return this.imports;
    }

    public /* varargs */ Fixture getFixture(String name, String ... parameters) {
        String fixtureName = this.buildName(name);
        SpyFixture fixture = this.fixtures.get(fixtureName);
        if (fixture == null) {
            fixture = new SpyFixture(fixtureName);
        }
        fixture.addConstructors(new Constructor(fixtureName, parameters.length));
        if (!StringUtil.isEmpty((String)name)) {
            this.fixtures.put(fixtureName, fixture);
        }
        return fixture;
    }

    public void addImport(String theImport) {
        this.imports.add(theImport);
    }

    private String buildName(String fixtureName) {
        if (!fixtureName.toLowerCase().endsWith("fixture")) {
            fixtureName = String.valueOf(fixtureName) + " fixture";
        }
        if (fixtureName.indexOf(".") > 0) {
            return NameUtils.toClassName((String)NameUtils.humanize((String)fixtureName.substring(fixtureName.lastIndexOf(".") + 1, fixtureName.length())));
        }
        return NameUtils.toClassName((String)NameUtils.humanize((String)fixtureName));
    }
}

