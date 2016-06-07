package com.greenpepper.maven.plugin.spy.impl;

import com.greenpepper.maven.plugin.spy.*;
import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.EnterRow;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.io.File.separatorChar;
import static java.lang.String.format;
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
        String packageName = getPackageName(fixture, systemUnderDevelopment);

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

        fileHasBeenUpdated |= updateConstructors(fixture, javaClass);

        fileHasBeenUpdated |= updateFields(fixture, javaClass);

        fileHasBeenUpdated |= updateMethods(fixture, fixtureSourceDirectory, javaClass);

        if (fileHasBeenUpdated) {
            switch (action) {
                case NONE:
                    action = ActionDone.UPDATED;
                    writeStringToFile(javaFile, javaClass.toUnformattedString());
                    break;
                case CREATED:
                    writeStringToFile(javaFile, javaClass.toString());
                    break;
                default:
                    throw new IllegalArgumentException("Action " + action + " is not supported.");
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
            } else {
                forceMkdir(fixtureSourceDirectory);
                javaSouceFile = new File(fixtureSourceDirectory, fixtureFilename);
                LOGGER.warn("You have no default package defined. I can't choose any import packages. Generating the Fixture in the source root. This is generally not a good idea.");
            }
        }
        return javaSouceFile ;
    }

    private String getPackageName(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment) {
        String packageName = null;
        Matcher matcher = FULL_CLASS_NAME_PATTERN.matcher(fixture.getRawName());
        if (matcher.matches()) {
            packageName = matcher.group(1);
        }
        if (isBlank(packageName)) {
            List<String> imports = systemUnderDevelopment.getImports();
            if (imports != null && imports.size() == 1 && isNotBlank(imports.get(0))) {
                packageName = imports.get(0);
            } else if (isNotBlank(defaultPackage)) {
                packageName = defaultPackage;
            }
        }
        return packageName;
    }

    /**
     *
     * @return true if an update has been made.
     */
    private boolean updateMethods(SpyFixture fixture, File fixtureSourceDirectory, JavaClassSource javaClass) throws FileNotFoundException {
        boolean fileHasBeenUpdated = false;
        switch (fixture.getType()) {
            case WORKFLOW:
                for (Method method : fixture.getMethods()) {
                    boolean existingMethodFound = isExistingMethodFound(fixtureSourceDirectory, javaClass, method);

                    if (!existingMethodFound) {
                        LOGGER.debug("Creating Method '{}' to deal with '{}'", method.getName(), method.getRawName() );
                        MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                                .setName(method.getName())
                                .setPublic();

                        SpyFixture subFixtureSpy = method.getSubFixtureSpy();
                        if (subFixtureSpy == null) {
                            methodSource.setReturnType(String.class);
                        } else {
                            switch (subFixtureSpy.getType()) {
                                case COLLECTION_PROVIDER:
                                    generateMethodForCollectionProvider(javaClass, methodSource, subFixtureSpy);
                                    break;
                                case SETUP:
                                    generateMethodForSetup(javaClass, methodSource, subFixtureSpy);
                                    break;
                                default:
                                    throw new IllegalStateException("The value '" + subFixtureSpy.getType() + "' of the subFixture is not supported");
                            }
                        }

                        for (int i = 0; i < method.getArity(); i++) {
                            methodSource.addParameter(String.class, "param" + (i + 1));
                        }
                        methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
                        fileHasBeenUpdated = true;
                    }

                }
                break;
            case COLLECTION_PROVIDER:
                Method queryMethod = new Method("query",0);
                boolean existingMethodFound = isExistingMethodFound(fixtureSourceDirectory, javaClass, queryMethod);
                if (!existingMethodFound) {
                    LOGGER.debug("Creating Method '{}' to provide the @CollectionProvider", queryMethod.getName() );
                    MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                            .setName(queryMethod.getName())
                            .setPublic();
                    generateMethodForCollectionProvider(javaClass, methodSource, fixture);
                    methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
                    fileHasBeenUpdated = true;
                }
                break;
            case SETUP:
                Method method = new Method("enter row", 0);
                boolean existingEnterRowMethodFound = isExistingMethodFound(fixtureSourceDirectory, javaClass, method);
                if (!existingEnterRowMethodFound) {
                    LOGGER.debug("Creating Method '{}' to provide the @EnterRow", method.getName() );
                    MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                            .setName(method.getName())
                            .setPublic();
                    generateMethodForSetup(javaClass, methodSource, fixture);
                    methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
                    fileHasBeenUpdated = true;
                }
                break;
            default:
                throw new IllegalStateException("The value '" + fixture.getType() + "' of the SpyFixture is not supported");

        }

        return fileHasBeenUpdated;
    }

    private void generateMethodForSetup(JavaClassSource javaClass, MethodSource<JavaClassSource> methodSource, SpyFixture subFixtureSpy) {
        LOGGER.debug("Processing @EnterRow method");
        methodSource.addAnnotation(EnterRow.class);
        appendFieldsToClass(javaClass, subFixtureSpy.getProperties());
    }

    private void generateMethodForCollectionProvider(JavaClassSource javaClass, MethodSource<JavaClassSource> methodSource, SpyFixture subFixtureSpy) {
        Pojo pojo = subFixtureSpy.getPojo();
        if (!javaClass.hasNestedType(pojo.getName())) {
            JavaSource<?> nestedType = javaClass.addNestedType(format("public static class %s {}", pojo.getName()));
            if (nestedType.isClass()) {
                JavaClassSource nestedType1 = (JavaClassSource) nestedType;
                for (Property property : pojo.getProperties()) {
                    nestedType1.addField().setName(property.getName()).setType(String.class).setPublic();
                }
            }
        }
        JavaSource<?> nestedType = javaClass.getNestedType(pojo.getName());
        methodSource.setReturnType(format("java.util.Collection<%s>",
                replaceChars(nestedType.getQualifiedName(), '$', DOT)))
                .addAnnotation(CollectionProvider.class);
    }

    private boolean isExistingMethodFound(File fixtureSourceDirectory, JavaClassSource javaClass, Method method) throws FileNotFoundException {
        LOGGER.debug("Searching method '{}' in class '{}'", method.getRawName(), javaClass.getName());
        boolean existingMethodFound = false;
        for (MethodSource<JavaClassSource> methodSource : javaClass.getMethods()) {
            // Normal case
            if (equalsIgnoreCase(method.getName(), methodSource.getName()) &&
                    methodSource.getParameters().size() == method.getArity()) {
                existingMethodFound = true;
                LOGGER.debug("Found Method '{}' to deal with '{}'", methodSource.getName(), method.getRawName() );
                break;
            }
            if (equalsIgnoreCase(method.getName(),"query") && methodSource.hasAnnotation(CollectionProvider.class)) {
                existingMethodFound = true;
                LOGGER.debug("Found Method '{}' to deal with '@CollectionProvider' in collection fixture", methodSource.getName() );
                break;
            }
            if (equalsIgnoreCase(method.getName(),"enterRow") && methodSource.hasAnnotation(EnterRow.class)) {
                existingMethodFound = true;
                LOGGER.debug("Found Method '{}' to deal with '@EnterRow' in setup fixture", methodSource.getName() );
                break;
            }
            SpyFixture subFixtureSpy = method.getSubFixtureSpy();
            if (subFixtureSpy != null) {
                switch (subFixtureSpy.getType()) {
                    case COLLECTION_PROVIDER:
                        existingMethodFound = checkForStandardMethod(method, methodSource, CollectionProvider.class, "query");
                        break;
                    case SETUP:
                        existingMethodFound = checkForStandardMethod(method, methodSource, EnterRow.class, "enterRow");
                        break;
                    default:
                        throw new IllegalStateException("The value '" + subFixtureSpy.getType() + "' of the subFixture is not supported");

                }
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

    private boolean checkForStandardMethod(Method method, MethodSource<JavaClassSource> methodSource, Class<? extends Annotation> annotation, String defaultMethodName) {
        boolean existingMethodFound = false;
        // method technical name
        if (equalsIgnoreCase(methodSource.getName(),defaultMethodName) && methodSource.getParameters().isEmpty()) {
            existingMethodFound = true;
            LOGGER.debug("Found Method '{}' to deal with '{}'", methodSource.getName(), method.getRawName() );
        } else if (methodSource.hasAnnotation(annotation)) {
            // annotation
            existingMethodFound = true;
            LOGGER.debug("Found Method '{}' to deal with '{}'", methodSource.getName(), method.getRawName() );
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
    private boolean updateFields(SpyFixture fixture, JavaClassSource javaClass) {
        Set<Property> properties = fixture.getProperties();
        return appendFieldsToClass(javaClass, properties);
    }

    private boolean appendFieldsToClass(JavaClassSource javaClass, Set<Property> properties) {
        boolean fileHasBeenUpdated = false;
        for (Property property : properties) {
            if (!hasFieldIgnoreCase(javaClass, property)){
                javaClass.addField()
                        .setName(property.getName())
                        .setType(String.class)
                        .setPublic();
                fileHasBeenUpdated = true;
            }
        }
        return fileHasBeenUpdated;
    }

    private boolean hasFieldIgnoreCase(JavaClassSource javaClass, Property property) {
        for (FieldSource<JavaClassSource> fieldSource : javaClass.getFields()) {
            if (equalsIgnoreCase(property.getName(), fieldSource.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return true if an update has been made.
     */
    private boolean updateConstructors(SpyFixture fixture, JavaClassSource javaClass) {
        boolean fileHasBeenUpdated = false;
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
