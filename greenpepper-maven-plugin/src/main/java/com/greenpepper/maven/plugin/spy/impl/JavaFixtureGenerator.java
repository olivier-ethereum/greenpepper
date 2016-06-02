package com.greenpepper.maven.plugin.spy.impl;

import com.greenpepper.maven.plugin.spy.*;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.Collection;

public class JavaFixtureGenerator implements FixtureGenerator {

    @Override
    public String generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment) {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        javaClass.setName(fixture.getName());
        Collection<String> imports = systemUnderDevelopment.getImports();
        if (!imports.isEmpty()) {
            javaClass.setPackage(imports.iterator().next());
        }

        for (Constructor constructor : fixture.getConstructors()) {
            MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                    .setPublic()
                    .setConstructor(true)
                    .setBody("throw new UnsupportedOperationException(\"Not yet implemented!\");");

            for (int i = 0; i < constructor.getArity(); i++) {
                methodSource.addParameter(String.class, "param" + (i+1) );
            }
        }

        for (Property property : fixture.getProperties()) {
            javaClass.addField()
                    .setName(property.getName())
                    .setType(String.class)
                    .setPublic();
        }

        for (Method method : fixture.getMethods()) {
            MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                    .setName(method.getName())
                    .setPublic()
                    .setReturnType(String.class);
            for (int i = 0; i < method.getArity(); i++) {
                methodSource.addParameter(String.class, "param" + (i+1) );
            }
            methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
        }

        return javaClass.toString();
    }
}
