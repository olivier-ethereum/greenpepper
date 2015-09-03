package com.greenpepper.maven.runner;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import com.greenpepper.GreenPepperCore;

public class ArgumentsParserTest {
    
    @Test
    public void shouldPprintHelpAndReturnNull() throws ParseException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        String[] args = new String[]{};
        CommandLine parse = commandLineParser.parse(args);
        assertNull(parse);
        String string = out.toString();
        assertTrue(string.contains("--help"));
    }

    @Test
    public void shouldPprintHelpAndReturnNullWhenHelpAsked() throws ParseException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        String[] args = new String[]{"-h", "-x"};
        CommandLine parse = commandLineParser.parse(args);
        assertNull(parse);
        String string = out.toString();
        System.out.println(string);
        assertTrue(string.contains("--help"));
    }
    
    @Test
    public void shouldThrowExceptionAndPrintHelpOnError(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        String[] args = new String[]{"-x", "--plop"};
        try {
            commandLineParser.parse(args);
            fail("Should have thrown an exception.");
        } catch (ParseException e) {
        }
        String string = out.toString();
        assertTrue(string.contains("--help"));
    }
    
    @Test
    public void shouldParseTheCommandLine() throws ParseException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        String cmd = "GP%20-%20Customer%20Management%20-%20Orders%20for%202nd%20Channel%20Customers%20-%20Manual%20order%202.15?implemented=false "
                + "/tmp/GreenPepperTest5842057936903784515.tst -l fr "
                + "-r com.greenpepper.runner.repository.AtlassianRepository;https://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#param;t1;passw0rd "
                + "-f com.strator.iris.greenpepper.IrisSpringSystemUnderDevelopmentWithPostgres;false;ES "
                + "--xml --pdd mydir/pom.xml";
        String[] args = cmd.split(" ");
        CommandLine cmdLine = commandLineParser.parse(args);
        assertTrue(cmdLine.hasOption("l"));
        assertEquals("fr", cmdLine.getOptionValue("l"));
        assertTrue(cmdLine.hasOption("r"));
        assertEquals("com.greenpepper.runner.repository.AtlassianRepository;https://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#param;t1;passw0rd", cmdLine.getOptionValue("r"));
        assertTrue(cmdLine.hasOption("f"));
        assertEquals("com.strator.iris.greenpepper.IrisSpringSystemUnderDevelopmentWithPostgres;false;ES", cmdLine.getOptionValue("f"));
        assertTrue(cmdLine.hasOption("x"));
        assertTrue(cmdLine.hasOption("p"));
        assertEquals("mydir/pom.xml", cmdLine.getOptionValue("p"));
        String[] nonoptions = cmdLine.getArgs();
        assertEquals(2, nonoptions.length);
        assertEquals("GP%20-%20Customer%20Management%20-%20Orders%20for%202nd%20Channel%20Customers%20-%20Manual%20order%202.15?implemented=false", nonoptions[0]);
        assertEquals("/tmp/GreenPepperTest5842057936903784515.tst", nonoptions[1]);
    }

    @Test
    public void shouldCreateTemplatingContext() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        String cmd = "MyInputPage "
                + "MyOutputPage -l fr "
                + "-r com.greenpepper.runner.repository.AtlassianRepository;https://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#param;t1;passw0rd "
                + "-f com.strator.iris.greenpepper.IrisSpringSystemUnderDevelopmentWithPostgres;false;ES "
                + "--xml --pdd greenpepper:greenpepper-confluence-demo:pom:" + GreenPepperCore.VERSION + " --dev --debug";
        String[] args = cmd.split(" ");
        CommandLine cmdLine = commandLineParser.parse(args);
        
        Map<String, String> context = commandLineParser.createTemplatingContext(cmdLine);
        assertEquals("MyInputPage", context.get("inputPage"));
        assertEquals("MyOutputPage", context.get("outputPage"));
        assertEquals("fr", context.get("locale"));
        assertEquals("com.strator.iris.greenpepper.IrisSpringSystemUnderDevelopmentWithPostgres;false;ES", context.get("sud"));
        assertEquals("com.greenpepper.runner.repository.AtlassianRepository", context.get("repositoryClass"));
        assertEquals("https://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#param;t1;passw0rd", context.get("repositoryRoot"));
        assertEquals("true", context.get("devmode"));
        assertEquals("true", context.get("debug"));
        assertEquals("greenpepper", context.get("groupId"));
        assertEquals("greenpepper-confluence-demo", context.get("artifactId"));
        assertEquals(GreenPepperCore.VERSION, context.get("version"));
        assertEquals(GreenPepperCore.VERSION, context.get("gpversion"));
    }
    
    @Test
    public void shouldCreateTemplatingContext1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        String cmd = "MyInputPage "
                + "-r com.greenpepper.runner.repository.AtlassianRepository;https://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#param;t1;passw0rd "
                + "-f com.strator.iris.greenpepper.IrisSpringSystemUnderDevelopmentWithPostgres;false;ES "
                + "--xml --pdd greenpepper:greenpepper-confluence-demo:pom:" + GreenPepperCore.VERSION ;
        String[] args = cmd.split(" ");
        CommandLine cmdLine = commandLineParser.parse(args);
        
        Map<String, String> context = commandLineParser.createTemplatingContext(cmdLine);
        assertEquals("MyInputPage", context.get("inputPage"));
        assertEquals("com.strator.iris.greenpepper.IrisSpringSystemUnderDevelopmentWithPostgres;false;ES", context.get("sud"));
        assertEquals("com.greenpepper.runner.repository.AtlassianRepository", context.get("repositoryClass"));
        assertEquals("https://localhost/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#param;t1;passw0rd", context.get("repositoryRoot"));
        assertEquals("greenpepper", context.get("groupId"));
        assertEquals("greenpepper-confluence-demo", context.get("artifactId"));
        assertEquals(GreenPepperCore.VERSION, context.get("version"));
        assertEquals(GreenPepperCore.VERSION, context.get("gpversion"));
    }
    
    @Test
    public void shouldReturnEmptyContextonCreateTemplatingContextWhenCommandLineIsNull() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArgumentsParser commandLineParser =  new ArgumentsParser(out);
        Map<String, String> context = commandLineParser.createTemplatingContext(null);
        assertNotNull(context);
        assertTrue(context.isEmpty());
    }
}
