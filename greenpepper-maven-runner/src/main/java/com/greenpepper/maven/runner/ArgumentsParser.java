package com.greenpepper.maven.runner;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.GreenPepperCore;

public class ArgumentsParser {

    private static final String DEFAULT_SUD = "com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment";
    private static final String DEFAULT_REPOSITORY = "com.greenpepper.repository.FileSystemRepository";
    private PrintWriter out;
    private Logger logger = LoggerFactory.getLogger(ArgumentsParser.class);

    public ArgumentsParser() {
        this(System.out);
    }

    public ArgumentsParser(OutputStream out) {
        this.out = new PrintWriter(out, true);
    }

    private Options buildCommandLineOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print the help");
        //options.addOption("d", "dev", false, "Sets the run in development mode. This means that the run output logs will be integrated inside the results page.");
        options.addOption("v", "debug", false, "Set the run to debug mode.");
        options.addOption("l", "locale", true, "Set the Locale");
        options.addOption("f", "sud", true, "Set the FixtureFactory. Defaults to " + DEFAULT_SUD);
        options.addOption("r", "repository", true, String.format("Set the repository to get the specs from. Defaults to %s;%s", DEFAULT_REPOSITORY,CommandLineRunner.CWD));
        options.addOption("x", "xml", false, "Output the report in XML");
        options.addOption("o", "output", true, "Output directory for the reports");
        options.addOption("m", "scope", true, "the scopes to use. multiple scopes should be separated with ';'");
        Option pddOption = new Option("p", "pdd", true, "[MANDATORY] Sets the maven project descriptor. Can be some maven coordonnates or a POM file. The coordonnates are of the form 'groupId:artifactId[:packaging][:classifier]:version'");
        pddOption.setRequired(true);
        options.addOption(pddOption);
        return options;
    }

    private void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(out, 180,
                "GreenPepper: <inputPath> [<outputPath>] [-l <locale>] [-r <repository>] [-f <fixtureFactory>] [--xml] --pdd <projectDependencyDescriptor>", getHelpHeader(), options,
                HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, getHelpFooter());
    }

    private String getHelpFooter() {
        String footer = "\n";
        return footer;
    }

    private String getHelpHeader() {
        String header = "\n GreenPepper version " + GreenPepperCore.VERSION;
        return header;
    }

    /**
     * @param args
     * @return null if the help is asked.
     * @throws ParseException
     */
    public CommandLine parse(String[] args) throws ParseException {
        // create the command line parser
        GnuParser parser = new GnuParser();
        Options buildCommandLineOptions = buildCommandLineOptions();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(buildCommandLineOptions, args);
            if (cmd.getOptions().length == 0 || cmd.hasOption("h")) {
                printHelp(buildCommandLineOptions);
                return null;
            }
            if (cmd.getArgList().isEmpty()) {
                throw new MissingOptionException("The 'inputPage' is mandatory! No page to run given");
            }
            return cmd;
        } catch (MissingOptionException e) {
            if (args == null || args.length == 0) {
                logger.debug("option --help has been set. Ignoring Missing Option exception");
                printHelp(buildCommandLineOptions);
                return cmd;
            }
            for (String arg : args) {
                if (StringUtils.equals("-h", arg) || StringUtils.equals("--help", arg)) {
                    logger.debug("option --help has been set. Ignoring Missing Option exception");
                    printHelp(buildCommandLineOptions);
                    return null;
                }
            }
            handleParseException(buildCommandLineOptions, e);
            throw e;
        } catch (ParseException e) {
            handleParseException(buildCommandLineOptions, e);
            throw e;
        }
    }

    private void handleParseException(Options buildCommandLineOptions, ParseException e) {
        out.println(e.getMessage());
        out.println();
        printHelp(buildCommandLineOptions);
    }

}
