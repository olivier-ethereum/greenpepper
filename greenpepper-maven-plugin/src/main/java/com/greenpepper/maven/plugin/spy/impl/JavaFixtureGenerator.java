package com.greenpepper.maven.plugin.spy.impl;

import com.greenpepper.maven.plugin.spy.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.io.IOException;
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
    public Result generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory) throws Exception {
        String packageName = getPackageName(fixture, systemUnderDevelopment);

        File javaFile = getJavaSouceFile(fixture, fixtureSourceDirectory, packageName);

        ActionDone action = ActionDone.NONE;
        boolean fileHasBeenUpdated = false;
        final JavaClassSource javaClass;
        if (javaFile.exists()) {
            javaClass = Roaster.parse(JavaClassSource.class, javaFile);
        } else {
            javaClass = Roaster.create(JavaClassSource.class);
            if (isNotEmpty(packageName)) {
                javaClass.setPackage(packageName);
            }
            javaClass.setName(fixture.getName());
            fileHasBeenUpdated = true;
            action = ActionDone.CREATED;
        }

        fileHasBeenUpdated |= updateConstructors(fixture, fileHasBeenUpdated, javaClass);

        fileHasBeenUpdated |= updateFields(fixture, fileHasBeenUpdated, javaClass);

        fileHasBeenUpdated |= updateMethods(fixture, fileHasBeenUpdated, javaClass);

        if (fileHasBeenUpdated) {
            writeStringToFile(javaFile, javaClass.toUnformattedString());
            if (action == ActionDone.NONE) {
                action = ActionDone.UPDATED;
            }
        }

        return new Result(action, javaFile);
    }

    private File getJavaSouceFile(SpyFixture fixture, File fixtureSourceDirectory, String packageName) throws IOException {
        File directoryForFixure = fixtureSourceDirectory;
        if (isNotEmpty(packageName)) {
            directoryForFixure = new File(fixtureSourceDirectory, replaceChars(packageName, '.', '/'));
            forceMkdir(directoryForFixure);
        }
        return new File(directoryForFixure, fixture.getName() + ".java");
    }

    private String getPackageName(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment) {
        Collection<String> imports = systemUnderDevelopment.getImports();
        String packageName = null;
        Matcher matcher = FULL_CLASS_NAME_PATTERN.matcher(fixture.getRawName());
        if (matcher.matches()) {
            packageName = matcher.group(1);
        }
        if (isEmpty(packageName) && !imports.isEmpty()) {
            packageName = imports.iterator().next();
        }
        return packageName;
    }

    /**
     *
     * @return true if an update has been made.
     */
    private boolean updateMethods(SpyFixture fixture, boolean fileHasBeenUpdated, JavaClassSource javaClass) {
        for (Method method : fixture.getMethods()) {
            boolean existingMethodFound = false;
            for (MethodSource<JavaClassSource> methodSource : javaClass.getMethods()) {
                if (StringUtils.equals(method.getName(), methodSource.getName()) &&
                        methodSource.getParameters().size() == method.getArity()) {
                    existingMethodFound = true;
                    break;
                }
            }
            if (!existingMethodFound) {
                MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                        .setName(method.getName())
                        .setPublic()
                        .setReturnType(String.class);
                for (int i = 0; i < method.getArity(); i++) {
                    methodSource.addParameter(String.class, "param" + (i + 1));
                }
                methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
                fileHasBeenUpdated = true;
            }

        }
        return fileHasBeenUpdated;
    }

    /**
     *
     * @return true if an update has been made.
     */
    private boolean updateFields(SpyFixture fixture, boolean fileHasBeenUpdated, JavaClassSource javaClass) {
        for (Property property : fixture.getProperties()) {
            if (!javaClass.hasField(property.getName())){
                javaClass.addField()
                        .setName(property.getName())
                        .setType(String.class)
                        .setPublic();
                fileHasBeenUpdated = true;
            }
        }
        return fileHasBeenUpdated;
    }

    /**
     *
     * @return true if an update has been made.
     */
    private boolean updateConstructors(SpyFixture fixture, boolean fileHasBeenUpdated, JavaClassSource javaClass) {
        for (Constructor constructor : fixture.getConstructors()) {
            if (constructor.getArity() > 0) {
                boolean existingConstructorFound = false;
                for (MethodSource<JavaClassSource> methodSource : javaClass.getMethods()) {
                    if (StringUtils.equals(constructor.getName(), methodSource.getName()) &&
                            methodSource.getParameters().size() == constructor.getArity()) {
                        existingConstructorFound = true;
                        break;
                    }
                }
                if (!existingConstructorFound) {
                    MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                            .setPublic()
                            .setConstructor(true)
                            .setBody("throw new UnsupportedOperationException(\"Not yet implemented!\");");

                    for (int i = 0; i < constructor.getArity(); i++) {
                        methodSource.addParameter(String.class, "param" + (i + 1));
                    }
                    fileHasBeenUpdated = true;
                }
            }
        }
        return fileHasBeenUpdated;
    }
}
