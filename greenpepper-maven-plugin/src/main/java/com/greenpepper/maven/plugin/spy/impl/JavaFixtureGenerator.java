package com.greenpepper.maven.plugin.spy.impl;

import com.greenpepper.maven.plugin.spy.*;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.replaceChars;

public class JavaFixtureGenerator implements FixtureGenerator {

    private static final Pattern FULL_CLASS_NAME_PATTERN = Pattern.compile("([\\p{Alnum}\\.]+)\\.[\\p{Alnum}]+");

    @Override
    public File generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory) throws Exception {
        Collection<String> imports = systemUnderDevelopment.getImports();
        String packageName = null;
        Matcher matcher = FULL_CLASS_NAME_PATTERN.matcher(fixture.getRawName());
        if (matcher.matches()) {
            packageName = matcher.group(1);
        }
        if (isEmpty(packageName) && !imports.isEmpty()) {
            packageName = imports.iterator().next();
        }
        File directoryForFixure = fixtureSourceDirectory;
        if (isNotEmpty(packageName)) {
            directoryForFixure = new File(fixtureSourceDirectory, replaceChars(packageName, '.', '/'));
            forceMkdir(directoryForFixure);
        }
        File javaFile = new File(directoryForFixure, fixture.getName() + ".java");
        final JavaClassSource javaClass;
        if (javaFile.exists()) {
            javaClass = Roaster.parse(JavaClassSource.class, javaFile);
        } else {
            javaClass = Roaster.create(JavaClassSource.class);
            if (isNotEmpty(packageName)) {
                javaClass.setPackage(packageName);
            }
            javaClass.setName(fixture.getName());
        }

        for (Constructor constructor : fixture.getConstructors()) {
            Class<String>[] paramTypes = new Class[constructor.getArity()];
            for (int i = 0; i < constructor.getArity(); i++) {
                paramTypes[i] = String.class;
            }
            if (!javaClass.hasMethodSignature(constructor.getName(), (Class<?>[]) paramTypes)) {
                MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                        .setPublic()
                        .setConstructor(true)
                        .setBody("throw new UnsupportedOperationException(\"Not yet implemented!\");");

                for (int i = 0; i < constructor.getArity(); i++) {
                    methodSource.addParameter(String.class, "param" + (i + 1));
                }
            }
        }

        for (Property property : fixture.getProperties()) {
            if (!javaClass.hasField(property.getName())){
                javaClass.addField()
                        .setName(property.getName())
                        .setType(String.class)
                        .setPublic();
            }
        }

        for (Method method : fixture.getMethods()) {
            Class<String>[] paramTypes = new Class[method.getArity()];
            for (int i = 0; i < method.getArity(); i++) {
                paramTypes[i] = String.class;
            }
            if (!javaClass.hasMethodSignature(method.getName(), (Class<?>[]) paramTypes)) {
                MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                        .setName(method.getName())
                        .setPublic()
                        .setReturnType(String.class);
                for (int i = 0; i < method.getArity(); i++) {
                    methodSource.addParameter(String.class, "param" + (i + 1));
                }
                methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
            }
        }

        writeStringToFile(javaFile, javaClass.toString());
        return javaFile;
    }
}
