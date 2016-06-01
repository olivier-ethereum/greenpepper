package com.greenpepper.maven.plugin;

import com.greenpepper.document.*;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.maven.plugin.spy.*;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class FixtureGeneratorMojoTest  extends AbstractMojoTestCase {

    private FixtureGeneratorMojo mojo;

    public void testGenerateFixture() throws Exception {
        URL pomPath = SpecificationNavigatorMojoTest.class.getResource("pom-genfixture.xml");
        //mojo = (FixtureGeneratorMojo) lookupMojo("generate-fixtures", URIUtil.decoded(pomPath.getPath()));

        SpySystemUnderDevelopment spySut = new SpySystemUnderDevelopment();
        GreenPepperInterpreterSelector interpreterSelector = new GreenPepperInterpreterSelector(spySut);
        Document doc = HtmlDocumentBuilder.tablesAndLists().build(new FileReader(loadSpecification()));
        doc.addFilter(new CommentTableFilter());
        doc.addFilter(new GreenPepperTableFilter(false));
        doc.execute(interpreterSelector);

        HashMap<String, SpyFixture> fixtures = spySut.getFixtures();
        for (String fixtureName : fixtures.keySet()) {
            SpyFixture spyFixture = fixtures.get(fixtureName);
            JavaClassSource classSource = genCode(spyFixture, spySut);
            System.out.println(classSource);
        }
    }

    private JavaClassSource genCode(SpyFixture spyFixture, SpySystemUnderDevelopment spySut) {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        javaClass.setName(spyFixture.getName());
        HashSet<String> imports = spySut.getImports();
        if (!imports.isEmpty()) {
            javaClass.setPackage(imports.iterator().next());
        }

        for (Constructor constructor : spyFixture.getConstructors()) {
            MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                    .setPublic()
                    .setConstructor(true)
                    .setBody("throw new UnsupportedOperationException(\"Not yet implemented!\");");

            for (int i = 0; i < constructor.getArity(); i++) {
                methodSource.addParameter(String.class, "param" + (i+1) );
            }
        }

        for (Property property : spyFixture.getProperties()) {
            javaClass.addField()
                    .setName(property.getName())
                    .setType(String.class)
                    .setPublic();
        }

        for (Method method : spyFixture.getMethods()) {
            MethodSource<JavaClassSource> methodSource = javaClass.addMethod()
                    .setName(method.getName())
                    .setPublic()
                    .setReturnType(String.class);
            for (int i = 0; i < method.getArity(); i++) {
                methodSource.addParameter(String.class, "param" + (i+1) );
            }
            methodSource.setBody("throw  new UnsupportedOperationException(\"Not yet implemented!\");");
        }

        return javaClass;
    }


    private File loadSpecification() {
        URL resource = getClass().getResource("specs/CollectionOfValuesSample.html");
        return FileUtils.toFile(resource);
    }

}