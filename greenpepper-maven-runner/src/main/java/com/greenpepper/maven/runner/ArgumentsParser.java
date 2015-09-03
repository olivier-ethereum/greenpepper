package com.greenpepper.maven.runner;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.GreenPepperCore;
import com.greenpepper.maven.runner.resolver.CombinedResolver;
import com.greenpepper.maven.runner.resolver.CombinedResolver.MavenGAV;
import com.greenpepper.maven.runner.resolver.CombinedResolver.ResolverException;

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
        options.addOption("d", "dev", false, "Sets the run in development mode. This means that the run output logs will be integrated inside the results page.");
        options.addOption("v", "debug", false, "Set the run to debug mode.");
        options.addOption("l", "locale", true, "Set the Locale");
        options.addOption("f", "fixturefactory", true, "Set the FixtureFactory. Defaults to " + DEFAULT_SUD);
        options.addOption("r", "repository", true, String.format("Set the repository to get the specs from. Defaults to %s;%s", DEFAULT_REPOSITORY,CommandLineRunner.CWD));
        options.addOption("x", "xml", false, "Output the report in XML");
        options.addOption("o", "outputdir", true, "Output directory for the reports");
        Option vvOption = new Option("vv", false, "Sets GreenPepper to TRACE. Can be very verbose.");
        options.addOption(vvOption);
        Option vvvOption = new Option("vvv", true, "Sets a specific logger to TRACE. Can be very verbose.");
        vvvOption.setArgName("logger.name");
        options.addOption(vvvOption);
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
                    return cmd;
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

    public Map<String, String> createTemplatingContext(CommandLine commandLine) throws ResolverException {
        HashMap<String, String> result = new HashMap<String, String>();
        if (commandLine == null) {
            return result;
        }

        String sud = commandLine.hasOption("f") ? commandLine.getOptionValue("f") : DEFAULT_SUD;
        result.put("sud", sud);

        String repository = commandLine.getOptionValue("r");
        if (!StringUtils.isEmpty(repository)) {
            String[] repoInfo = repository.split(";", 2);
            result.put("repositoryClass", repoInfo[0]);
            if (repoInfo.length > 1) {
                result.put("repositoryRoot", repoInfo[1]);
            }
        } else {
            String currentDirectory = System.getProperty("user.dir", "");
            File currentDirectoryFile = new File(currentDirectory);
            logger.info("Settings default values for repository: \n\t- repositoryClass : {} \n\t- repositoryRoot : {}", DEFAULT_REPOSITORY, currentDirectoryFile.getAbsolutePath());
            result.put("repositoryClass", DEFAULT_REPOSITORY);
            result.put("repositoryRoot", currentDirectoryFile.getAbsolutePath());
        }
        if (commandLine.hasOption("l")) {
            result.put("locale", commandLine.getOptionValue("l"));
        }
        result.put("devmode", Boolean.toString(commandLine.hasOption("d")));
        result.put("debug", Boolean.toString(commandLine.hasOption("v")));
        result.put("gpversion", GreenPepperCore.VERSION);

        if (commandLine.hasOption("p")) {
            String projectDescriptor = commandLine.getOptionValue("p");
            CombinedResolver resolver = new CombinedResolver();
            MavenGAV gav = resolver.resolve(projectDescriptor);
            result.put("artifactId", gav.getArtifactId());
            result.put("groupId", gav.getGroupId());
            result.put("version", gav.getVersion());
            result.put("packaging", gav.getPackaging());
            result.put("classifier", gav.getClassifier());
        }
        if (commandLine.hasOption("o")) {
            result.put("reportsDirectory", commandLine.getOptionValue("o"));
        }

        String[] args = commandLine.getArgs();
        if (args != null && args.length >= 1) {
            result.put("inputPage", args[0]);
            if (args.length >= 2) {
                result.put("outputPage", args[1]);
            }
        }
        return result;
    }

}
