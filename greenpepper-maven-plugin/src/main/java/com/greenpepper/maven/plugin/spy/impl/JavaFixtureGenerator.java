package com.greenpepper.maven.plugin.spy.impl;

import com.greenpepper.maven.plugin.spy.*;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.util.Collection;

import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.replaceChars;

public class JavaFixtureGenerator implements FixtureGenerator {

    @Override
    public File generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory) throws Exception {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        javaClass.setName(fixture.getName());
        Collection<String> imports = systemUnderDevelopment.getImports();
        String packageName = null;
        if (!imports.isEmpty()) {
            packageName = imports.iterator().next();
            javaClass.setPackage(packageName);
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

        File directoryForFixure = fixtureSourceDirectory;
        if (isNotEmpty(packageName)) {
            directoryForFixure = new File(fixtureSourceDirectory, replaceChars(packageName, '.', '/'));
            forceMkdir(directoryForFixure);
        }
        File javaFile = new File(directoryForFixure, fixture.getName() + ".java");
        writeStringToFile(javaFile, javaClass.toString());
        return javaFile;
    }
}
