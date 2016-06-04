package com.greenpepper.maven.plugin.spy.impl;

import com.greenpepper.maven.plugin.spy.*;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.io.File.separatorChar;
import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

public class JavaFixtureGenerator implements FixtureGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaFixtureGenerator.class);

    private static final Pattern FULL_CLASS_NAME_PATTERN = Pattern.compile("([\\p{Alnum}\\.]+)\\.[\\p{Alnum}]+");
    private static final char DOT = '.';
    private static final String JAVA_EXTENSION = ".java";

    public String defaultPackage;

    @Override
    public Result generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory) throws Exception {
        String packageName = getPackageName(fixture);

        File javaFile = getJavaSouceFile(fixture, systemUnderDevelopment, fixtureSourceDirectory, packageName);

        ActionDone action = ActionDone.NONE;
        boolean fileHasBeenUpdated = false;
        final JavaClassSource javaClass;
        if (javaFile.exists()) {
            javaClass = Roaster.parse(JavaClassSource.class, javaFile);
        } else {
            javaClass = Roaster.create(JavaClassSource.class);
            if (isNotBlank(packageName)) {
                javaClass.setPackage(packageName);
            }
            javaClass.setName(fixture.getName());
            fileHasBeenUpdated = true;
            action = ActionDone.CREATED;
        }

        fileHasBeenUpdated |= updateConstructors(fixture, fileHasBeenUpdated, javaClass);

        fileHasBeenUpdated |= updateFields(fixture, fileHasBeenUpdated, javaClass);

        fileHasBeenUpdated |= updateMethods(fixture, fixtureSourceDirectory, fileHasBeenUpdated, javaClass);

        if (fileHasBeenUpdated) {
            writeStringToFile(javaFile, javaClass.toUnformattedString());
            if (action == ActionDone.NONE) {
                action = ActionDone.UPDATED;
            }
        }

        return new Result(action, javaFile);
    }

    private File getJavaSouceFile(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory, String packageName) throws IOException {
        String fixtureFilename = fixture.getName() + JAVA_EXTENSION;
        File javaSouceFile = null;
        // we search in every imports
        List<String> imports = systemUnderDevelopment.getImports();
        for (String anImport : imports) {
            File packageDir = new File(fixtureSourceDirectory, replaceChars(anImport, DOT, separatorChar));
            if (packageDir.isDirectory()) {
                Collection<File> files = listFiles(packageDir, new NameFileFilter(fixtureFilename, IOCase.INSENSITIVE), null);
                if (files.size() == 1) {
                    javaSouceFile = files.iterator().next();
                    break;
                } else if (files.size() > 1) {
                    LOGGER.error("You have multiple java sources with the same case insensitive names.");
                    LOGGER.error("We can't choose the file to merge the fixture in.");
                    LOGGER.error("Moreover, your build is platform dependant because of this issue.");
                    LOGGER.error("Incriminating files:");
                    for (File file : files) {
                        LOGGER.error("\t - {}", file.getAbsolutePath());
                    }
                    break;
                }
            }
        }
        // if we didn't find the file
        if (javaSouceFile == null) {
            if (isNotBlank(packageName)) {
                File directoryForFixure = new File(fixtureSourceDirectory, replaceChars(packageName, DOT, separatorChar));
                forceMkdir(directoryForFixure);
                javaSouceFile = new File(directoryForFixure, fixtureFilename);
            } else if (imports.size() == 1 && isNotBlank(imports.get(0))) {
                File directoryForFixure = new File(fixtureSourceDirectory, replaceChars(imports.get(0), DOT, separatorChar));
                forceMkdir(directoryForFixure);
                javaSouceFile = new File(directoryForFixure, fixtureFilename);
            } else {
                forceMkdir(fixtureSourceDirectory);
                javaSouceFile = new File(fixtureSourceDirectory, fixtureFilename);
                LOGGER.warn("You have no default package defined. I can't choose any import packages. Generating the Fixture in the source root. This is generally not a good idea.");
            }
        }
        return javaSouceFile ;
    }

    private String getPackageName(SpyFixture fixture) {
        String packageName = null;
        Matcher matcher = FULL_CLASS_NAME_PATTERN.matcher(fixture.getRawName());
        if (matcher.matches()) {
            packageName = matcher.group(1);
        }
        if (isBlank(packageName) && isNotBlank(defaultPackage)) {
            packageName = defaultPackage;
        }
        return packageName;
    }

    /**
     *
     * @return true if an update has been made.
     */
    private boolean updateMethods(SpyFixture fixture, File fixtureSourceDirectory, boolean fileHasBeenUpdated, JavaClassSource javaClass) throws FileNotFoundException {
        for (Method method : fixture.getMethods()) {
            boolean existingMethodFound = isExistingMethodFound(fixtureSourceDirectory, javaClass, method);

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

    private boolean isExistingMethodFound(File fixtureSourceDirectory, JavaClassSource javaClass, Method method) throws FileNotFoundException {
        LOGGER.debug("Searching method {} in class {}", method.getName(), javaClass.getName());
        boolean existingMethodFound = false;
        for (MethodSource<JavaClassSource> methodSource : javaClass.getMethods()) {
            if (StringUtils.equals(method.getName(), methodSource.getName()) &&
                    methodSource.getParameters().size() == method.getArity()) {
                existingMethodFound = true;
                LOGGER.debug("Method Found");
                break;
            }
        }
        if (!existingMethodFound) {
            // Find on the method on the superclass
            String superType = javaClass.getSuperType();
            if (isNotBlank(superType) && !StringUtils.equals("java.lang.Object", superType)) {
                JavaClassSource superTypeSource = findTheClass(fixtureSourceDirectory, superType);
                if (superTypeSource != null) {
                    existingMethodFound = isExistingMethodFound(fixtureSourceDirectory, superTypeSource, method);
                }
            }
        }

        return existingMethodFound;
    }

    private JavaClassSource findTheClass(File fixtureSourceDirectory, String superType) throws FileNotFoundException {
        File javaSourceFile = getFile(fixtureSourceDirectory, replaceChars(superType, DOT, separatorChar) + JAVA_EXTENSION);
        if (javaSourceFile.exists()) {
            return Roaster.parse(JavaClassSource.class, javaSourceFile);
        } else {
            LOGGER.info("I can't find the source of '{}' in the source directory '{}'", superType, fixtureSourceDirectory);
        }
        return null;
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
