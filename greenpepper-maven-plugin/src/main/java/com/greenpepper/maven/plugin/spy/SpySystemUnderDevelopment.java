package com.greenpepper.maven.plugin.spy;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.NameUtils;
import com.greenpepper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SpySystemUnderDevelopment extends DefaultSystemUnderDevelopment {
    private HashMap<String, SpyFixture> fixtures = new HashMap<String, SpyFixture>();
    private List<String> imports = new ArrayList<String>();

    public HashMap<String, SpyFixture> getFixtures() {
        return this.fixtures;
    }

    public List<String> getImports() {
        return this.imports;
    }

    public Fixture getFixture(String name, String ... parameters) {
        String fixtureName = this.buildName(name);
        SpyFixture fixture = this.fixtures.get(fixtureName);
        if (fixture == null) {
            fixture = new SpyFixture(fixtureName);
            fixture.setRawName(name);
        }
        fixture.addConstructors(new Constructor(fixtureName, parameters.length));
        if (!StringUtil.isEmpty(name)) {
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
            return NameUtils.toClassName(NameUtils.humanize(fixtureName.substring(fixtureName.lastIndexOf(".") + 1, fixtureName.length())));
        }
        return NameUtils.toClassName(NameUtils.humanize(fixtureName));
    }
}

