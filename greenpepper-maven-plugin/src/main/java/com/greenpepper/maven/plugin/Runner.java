package com.greenpepper.maven.plugin;

import com.greenpepper.GreenPepper;
import com.greenpepper.GreenPepperCore;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.rpc.runner.report.XmlReport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Runner extends com.greenpepper.server.domain.Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    SpecificationRunnerMonitor monitor;

    SpecificationRunnerMojo.ReportType reportType;

    static Runner createDefault(String jvm, List<String> optionsList) {
        Runner defaultRunner = new Runner();
        defaultRunner.setEnvironmentType(EnvironmentType.newInstance("JAVA"));
        defaultRunner.setName("GP Core " + GreenPepperCore.VERSION);
        defaultRunner.setMainClass("com.greenpepper.runner.Main");
        String cmdLineTemplate = format("%s -mx252m -cp ${classpaths} ${mainClass} ${inputPath} ${outputPath} "
                + "-r ${repository} -f ${fixtureFactory} --xml %s", jvm, StringUtils.join(optionsList, " "));
        defaultRunner.setCmdLineTemplate(cmdLineTemplate);
        return defaultRunner;
    }

    public void execute(Specification specification, SystemUnderTest systemUnderTest, String outputPath) throws IOException {
        monitor.testRunning(specification.getName());
        Execution execution;
        // by setting imlemented version to true, we rely on the specification name itself to hold the implemented tag
        if(isRemote()) {
            execution = executeRemotely(specification, systemUnderTest, true, null, null);
        } else {
            execution = executeLocally(specification, systemUnderTest, true, null, null, outputPath + ".xml");
        }
        if (isNotBlank(execution.getExecutionErrorId())) {
            LOGGER.error("Failed to execute the specifcation : " + execution.getExecutionErrorId());
        }

        monitor.testDone(execution.getSuccess(), execution.getFailures(), execution.getErrors(),execution.getIgnored());

        switch (reportType) {
            case html:
                FileUtils.writeStringToFile(new File(outputPath), execution.getCleanedResults());
                break;
            case xml:
                XmlReport xmlReport = XmlReport.newInstance(getName());
                xmlReport.generate(execution);
                FileOutputStream outputStream = new FileOutputStream(new File(outputPath + ".xml"));
                PrintWriter printWriter = new PrintWriter(outputStream);
                xmlReport.printTo(printWriter);
                printWriter.flush();
                printWriter.close();
                break;
            default:
                throw new IllegalArgumentException(format("Report type %s is not supported!", reportType));
        }
    }

    void setReportType(SpecificationRunnerMojo.ReportType reportType) {
        this.reportType = reportType;
    }
}