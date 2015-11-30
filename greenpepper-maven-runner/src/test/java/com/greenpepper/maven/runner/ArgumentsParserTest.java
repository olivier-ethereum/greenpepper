package com.greenpepper.maven.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

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

}
