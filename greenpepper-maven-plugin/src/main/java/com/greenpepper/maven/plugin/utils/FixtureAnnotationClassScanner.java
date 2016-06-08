package com.greenpepper.maven.plugin.utils;

import com.google.common.collect.Multimap;
import com.greenpepper.annotation.Fixture;
import com.greenpepper.annotation.FixtureCollection;
import com.greenpepper.annotation.FixtureMethod;
import com.greenpepper.maven.plugin.schemas.*;
import com.greenpepper.reflect.CollectionProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.reflections.ReflectionUtils.*;

public class FixtureAnnotationClassScanner {

    private static final String CAT_FIXTURES_ABSTRAITES = "Fixtures abstraites";

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FixtureAnnotationClassScanner.class);

    private static Map<String, String> xsdTypeMap;
    static {
        xsdTypeMap = new HashMap<String, String>();
        xsdTypeMap.put("String", "string");
        xsdTypeMap.put("Integer", "integer");
        xsdTypeMap.put("Long", "long");
        xsdTypeMap.put("Double", "double");
        xsdTypeMap.put("Float", "float");
        xsdTypeMap.put("Boolean", "boolean");
    }

    /**
     * @param packagesToScan the package to scan for fixtures
     * @return The full Fixtures description.
     * @throws ClassNotFoundException
     */
    public Fixtures scan(String ... packagesToScan) throws ClassNotFoundException {
        ConfigurationBuilder configuration = new ConfigurationBuilder();
        FilterBuilder filterBuilder = new FilterBuilder();
        for (String packageToScan : packagesToScan) {
            configuration.addUrls(ClasspathHelper.forPackage(packageToScan));
            filterBuilder.include(replace(packageToScan,".", "\\.") + "\\..+Fixture\\.class");
        }
        configuration.filterInputsBy(filterBuilder);
        configuration.addScanners(new JavaClassesScanner());
        Reflections reflections = new Reflections(configuration);

        Store store = reflections.getStore();
        Multimap<String, String> fixturesClassFilesMap = store.get(JavaClassesScanner.class.getSimpleName());
        Collection<String> fixturesClassFiles = fixturesClassFilesMap.values();
        LOGGER.info("Found {} Fixture classes.", fixturesClassFiles.size());
        if (LOGGER.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder();
            for (String fixturesClassFile : fixturesClassFiles) {
                builder.append("\n\t ").append(fixturesClassFile);
            }
            LOGGER.debug(builder.toString());
        }

        Fixtures fixturesType = new Fixtures();
        fixturesType.setLanguage(TypeLanguage.JAVA);
        List<FixtureType> listFixtureType = fixturesType.getFixture();

        for (String fixturesClassFile : fixturesClassFiles) {
            LOGGER.debug("Scanning {}", fixturesClassFile);
            Class<?> clazz = Class.forName(fixturesClassFile);
            if (Modifier.isAbstract(clazz.getModifiers())) {
                if (isAbstractFixture(clazz)) {
                    LOGGER.debug("Parsing Abstract fixture {}", clazz);
                } else {
                    LOGGER.debug("Skipping abstract Class {}", clazz);
                    continue;
                }
            }
            FixtureType fixtureType = new FixtureType();

            if (clazz.isAnnotationPresent(Fixture.class)) {
                LOGGER.debug("Parsing @Fixture annotation");
                parseFixtureAnnotation(clazz, fixtureType);
            }

            List<MembreType> listMembreType = fixtureType.getMembre();

            LOGGER.debug(" Retrieve and add constructor members");
            Constructor<?>[] constructors = clazz.getConstructors();
            addClassMembreType(listMembreType, constructors, true);

            LOGGER.debug(" Retrieve and add method members");
            Set<Method> methods = getAllMethods(clazz, withAnnotation(FixtureMethod.class));
            addMethodMembreTypes(listMembreType, methods);

            LOGGER.debug(" Retrieve and add public field members");
            Set<Field> publicFields = getAllFields(clazz, withModifier(Modifier.PUBLIC));
            addFieldMembreTypes(listMembreType, publicFields);

            fixtureType.setClasse(returnTypeToString(clazz));
            listFixtureType.add(fixtureType);

            LOGGER.debug(" Retrieve and add new fixture with method members returning a Collection");
            Set<Method> collectionProviderMethods = getAllMethods(clazz, withReturnTypeAssignableTo(Collection.class));
            addCollectionProviderFixtureType(clazz, listFixtureType, collectionProviderMethods);
            LOGGER.debug(" Retrieve and add new fixture with method members annotated with @CollectionProvider or with signature query()");
            collectionProviderMethods = getAllMethods(clazz, withAnnotation(CollectionProvider.class));
            addCollectionProviderFixtureType(clazz, listFixtureType, collectionProviderMethods);
            LOGGER.debug(" Retrieve and add new fixture with method with signature query()");
            collectionProviderMethods = getAllMethods(clazz, and(withName("query"), withParametersCount(0)));
            addCollectionProviderFixtureType(clazz, listFixtureType, collectionProviderMethods);
        }

        return fixturesType;
    }

    private boolean isAbstractFixture(Class<?> clazz) {
        return !(clazz.isEnum() || clazz.isAnnotation() || clazz.isInterface())
                && clazz.isAnnotationPresent(Fixture.class) && clazz.getAnnotation(Fixture.class).isAbstract();
    }

    private void parseFixtureAnnotation(Class<?> clazz, FixtureType fixtureType) {
        Fixture annotation = clazz.getAnnotation(Fixture.class);
        if (annotation.obsolete()) {
            LOGGER.debug("Found Obsolete Fixture");
            fixtureType.setObsolete(true);
        } else {
            fixtureType.setValidee(annotation.validate());
        }
        if (StringUtils.isNotBlank(annotation.value())) {
            fixtureType.setMessage(annotation.value());
        }
        if (annotation.obsolete()) {
            Class<?>[] relatedTo = annotation.relatedTo();
            if (!ArrayUtils.isEmpty(relatedTo)) {
                fixtureType.setFixturederemplacement(relatedTo[0].getSimpleName());
                String message = "";
                if (StringUtils.isNotBlank(annotation.value())) {
                    message = annotation.value();
                }
                StringBuilder buffer = new StringBuilder(message).append(" (Replacing Fixture: ");
                for (Class<?> class1 : relatedTo) {
                    buffer.append(class1.getSimpleName()).append(" ");
                }
                buffer.append(")");
                fixtureType.setMessage(buffer.toString());
            }
        }

        if (StringUtils.isNotBlank(annotation.category())) {
            fixtureType.setCategorie(annotation.category());
        }

        if (StringUtils.isNotBlank(annotation.usage())) {
            fixtureType.setUsage(annotation.usage());
        }
        if (annotation.correctionPrioritaire()) {
            fixtureType.setCorrectionprioritaire(true);
        }
        if (annotation.isAbstract()) {
            fixtureType.setCategorie(CAT_FIXTURES_ABSTRAITES);
        }
    }

    /**
     * Parsing des FixtureCollection.
     * @param clazz
     * @param listFixtureType
     * @param methods
     */
    private void addCollectionProviderFixtureType(Class<?> clazz, List<FixtureType> listFixtureType, Set<Method> methods) {

        for (Method method : methods) {
            if (Collection.class.isAssignableFrom(method.getReturnType())) {
                FixtureType fixtureType = new FixtureType();
                if (method.isAnnotationPresent(FixtureCollection.class)) {
                    FixtureCollection annotation = method.getAnnotation(FixtureCollection.class);
                    if (annotation.obsolete()) {
                        fixtureType.setObsolete(annotation.obsolete());
                    } else {
                        fixtureType.setValidee(annotation.validate());
                    }
                }

                Type genericReturnType = method.getGenericReturnType();
                Class<?> typeClass;
                if (ParameterizedType.class.isAssignableFrom(genericReturnType.getClass())) {
                    ParameterizedType parameterizedReturnType = (ParameterizedType) genericReturnType;
                    LOGGER.debug("Checking the Collection Generic type: {}", parameterizedReturnType);
                    typeClass = (Class<?>) parameterizedReturnType.getActualTypeArguments()[0];
                } else {
                    // We take the Object class
                    typeClass = Object.class;
                }

                fixtureType.setClasse(returnTypeToString(clazz) + "/" + method.getName());
                List<MembreType> listMembreType = fixtureType.getMembre();

                if (Modifier.isAbstract(clazz.getModifiers())) {
                    fixtureType.setCategorie(CAT_FIXTURES_ABSTRAITES);
                } else {
                    Set<Field> allFields = getAllFields(typeClass,
                            and(or(withModifier(Modifier.PUBLIC), withModifier(Modifier.PRIVATE)),
                                    not(withModifier(Modifier.STATIC))));

                    for (Field field : allFields) {
                        MembreType membreType = new MembreType();
                        membreType.setNom(field.getName());
                        membreType.setType(TypeMembreEnum.QUERY);
                        membreType.setTyperenvoye(returnTypeToString(field.getType().getSimpleName()));
                        listMembreType.add(membreType);
                    }
                }

                listFixtureType.add(fixtureType);
            } else {
                LOGGER.warn("A method matching the CollectionIterpreter is not returning a Collection: {}", method.toString());
            }
        }

    }

    private void addMethodMembreTypes(List<MembreType> listMembreType, Set<Method> methods) {
        if (methods == null) {
            return;
        }
        String returnTypeString = "";
        for (Method method : methods) {

            returnTypeString = returnTypeToString(method.getGenericReturnType());
            if (!(method.getGenericReturnType() instanceof ParameterizedType)
                    && !StringUtils.contains(returnTypeString, '/')) {
                MembreType membreType = createMembreType(TypeMembreEnum.METHODE,
                        returnTypeToString(method.getGenericReturnType()), method.getName());
                FixtureMethod annotation = method.getAnnotation(FixtureMethod.class);
                if (annotation.obsolete()) {
                    membreType.setValidee(false);
                    if (StringUtils.isNotBlank(annotation.replacedWith())) {
                        membreType.setMessage("Replacing Method : " + annotation.replacedWith());
                    }
                    membreType.setObsolete(true);
                }
                if (annotation.validated()) {
                    membreType.setValidee(true);
                }
                if (StringUtils.isNotBlank(annotation.usage())) {
                    membreType.setUsage(annotation.usage());
                }

                List<ParametreType> listParameterType = membreType.getParametre();

                for (Class<?> clazzType : method.getParameterTypes()) {
                    ParametreType paramType = new ParametreType();
                    paramType.setType(returnTypeToString(clazzType));
                    listParameterType.add(paramType);
                }
                listMembreType.add(membreType);
            }
        }
    }

    private void addFieldMembreTypes(List<MembreType> listMembreType, Set<Field> fields) {
        if (fields == null) {
            return;
        }
        for (Field field : fields) {
            listMembreType.add(createMembreType(TypeMembreEnum.PROPRIETE, returnTypeToString(field.getType()),
                    field.getName()));
        }
    }

    private void addClassMembreType(List<MembreType> listMembreType, Constructor<?>[] constructors,
                                    boolean scanEmptyConstructor) {

        for (Constructor<?> constructor : constructors) {
            if (!scanEmptyConstructor && constructor.getParameterTypes().length == 0) {
                continue;
            }
            MembreType membreType = createMembreType(TypeMembreEnum.CONSTRUCTEUR, null,
                    returnTypeToString(constructor.getName()));
            List<ParametreType> listParametreType = membreType.getParametre();

            for (Class<?> clazz : constructor.getParameterTypes()) {
                ParametreType paramType = new ParametreType();
                paramType.setType(returnTypeToString(clazz));
                listParametreType.add(paramType);
            }
            listMembreType.add(membreType);
        }
    }

    private MembreType createMembreType(TypeMembreEnum typeMembreEnum, String typeRenvoye, String nom) {
        MembreType membreType = new MembreType();
        membreType.setType(typeMembreEnum);
        membreType.setTyperenvoye(typeRenvoye);
        membreType.setNom(nom);
        return membreType;

    }

    private String returnTypeToString(Type type) {
        if (type == null) {
            return "undefined";
        }
        if (type instanceof ParameterizedType) {

            Type type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            String[] strings = StringUtils.split(type2.toString(), '.');
            String strToReturn = strings[strings.length - 1];
            strToReturn = replace(strToReturn, "$", "/");
            return (xsdTypeMap.get(strToReturn) == null) ? strToReturn : xsdTypeMap.get(strToReturn);
        }

        String[] strings = StringUtils.split(type.toString(), '.');
        String strToReturn = strings[strings.length - 1];
        return (xsdTypeMap.get(strToReturn) == null) ? strToReturn : xsdTypeMap.get(strToReturn);

    }

    private String returnTypeToString(Class<?> clazz) {
        if (clazz == null) {
            return "undefined";
        }

        String simpleName = clazz.getSimpleName();
        return (xsdTypeMap.get(simpleName) == null) ? clazz.getSimpleName() : xsdTypeMap.get(clazz.getSimpleName());

    }

    private String returnTypeToString(String className) {
        if (className == null) {
            return "undefined";
        }
        String[] strings = StringUtils.split(className, '.');
        String strToReturn = strings[strings.length - 1];
        return (xsdTypeMap.get(strToReturn) == null) ? strToReturn : xsdTypeMap.get(strToReturn);
    }

}
