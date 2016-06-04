package com.greenpepper.maven.plugin.utils;

import org.reflections.scanners.AbstractScanner;

public class JavaClassesScanner extends AbstractScanner {

    @Override
    @SuppressWarnings("unchecked")
    public void scan(Object cls) {
        String className = getMetadataAdapter().getClassName(cls);
        String[] split = className.split("\\.");
        getStore().put(split[split.length - 1], className);
    }
}
