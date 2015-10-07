package com.greenpepper.maven.plugin.utils;

import java.util.Properties;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.introspection.ReflectionValueExtractor;

public class ReflectionProperties extends Properties {


    private static final long serialVersionUID = 2513486319080850421L;

    private MavenProject project;

    private Log logger;

    public ReflectionProperties(MavenProject aProject, Log logger) {
        super();

        project = aProject;
        this.logger = logger;
    }

    public Object get(Object key) {
        Object value = null;
        try {
            value = ReflectionValueExtractor.evaluate(String.valueOf(key), project);

        } catch (Exception e) {
            logger.debug("could not evaluate " + key, e);
        }
        return value;
    }
}
