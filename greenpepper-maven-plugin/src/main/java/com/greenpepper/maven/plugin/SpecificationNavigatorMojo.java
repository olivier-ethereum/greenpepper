package com.greenpepper.maven.plugin;

import com.greenpepper.server.domain.DocumentNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * List the Specifications from the configured repositories.
 *
 * @goal tree
 *
 */
public class SpecificationNavigatorMojo extends AbstractMojo {

    private static final Pattern filterPattern = Pattern.compile("(\\[!?I\\])?(\\[RE\\])?(.*)?");


    // "  [%04d] - [%11s] - [%s]"
    private static final Pattern INDEX_LINE_PATTERN = Pattern.compile("  \\[[0-9]{4}\\] - \\[.{11}\\] - \\[(.+)\\]$");

    /**
     * @parameter property="greenpepper.repositories"
     * @required
     */
    ArrayList<Repository> repositories;

    /**
     * Set this to a Repository name defined in the pom.xml.
     *
     * @parameter property="gp.repo"
     */
    String selectedRepository;

    /**
     * The directory where compiled fixture classes go.
     *
     * @parameter default-value="${project.build.directory}/greenpepper"
     * @required
     * @readonly
     */
    File specOutputDirectory;

    /**
     * Sets a filter to filter the output of the specs. The filter should have a specific syntax:
     * <ul>
     *     <li><code>"substring"</code> : a string to look for inside the page name. The search is case insensitive</li>
     *     <li><code>"[RE]regular expression"</code> : a regular expression that will be used to match the page name</li>
     * </ul>
     * Additionnally you can filter on the <code>implemented</code> status of the page by adding a <code>"[I]"</code>
     * as a prefix to your search filter.
     * <ul>
     *     <li><code>[I]</code> : Give the implemented pages only</li>
     *     <li><code>[!I]</code> : Give the non implemented pages only</li>
     * </ul>
     * <u>Note:</u> A <code>"[I]"</code> or <code>"[!I]"</code> as a search filter will filter only on the implemented status.
     * <br/>
     * Examples:
     * <ul>
     *     <li><code>sun</code> : all specifications having the substring 'sun'</li>
     *     <li><code>[RE]taurus</code> : the specification matching exactly 'taurus'</li>
     *     <li><code>[I]</code> : all implemented specifications</li>
     *     <li><code>[!I]</code> : all non implemented specifications</li>
     *     <li><code>[!I]dummy</code> : all non implemented specifications having the substring 'dummy'</li>
     *     <li><code>[I][RE]'.*moon[^dab]+'</code> : all implemented specifications having the RE '.*moon[^dab]+'</li>
     * </ul>
     *
     * @parameter property="gp.specFilter"
     */
    String specFilter;

    /**
     * Refresh the specificaction list (updating the index file)
     *
     * @parameter property="greenpepper.refresh" default-value="false"
     */
    boolean refresh;


    private PrintWriter writer;
    private ByteArrayOutputStream tempOutput;

    public SpecificationNavigatorMojo() {
        this.repositories = new ArrayList<Repository>();
        tempOutput = new ByteArrayOutputStream();
        TeeOutputStream teeOutputStream = new TeeOutputStream(System.out, tempOutput);
        this.writer = new PrintWriter(teeOutputStream);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        printBanner();
        processAllRepositories();
    }

    private void processAllRepositories() throws MojoExecutionException, MojoFailureException {
        boolean atLeastOneRepositoryProcessed = false;
        try {
            for (Repository repository : repositories) {
                if (isNotEmpty(selectedRepository)) {
                    if (StringUtils.equals(selectedRepository, repository.getName())) {
                        listRepositorySpecifications(repository);
                        atLeastOneRepositoryProcessed = true;
                        break;
                    } else {
                        getLog().debug(format("Skipping repository '%s', selected is '%s' ", repository.getName(), selectedRepository));
                    }
                } else {
                    listRepositorySpecifications(repository);
                }
            }
        } catch (MojoExecutionException e) {
            throw e;
        } catch (MojoFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("Error running the Goal", e);
        }
        if (isNotEmpty(selectedRepository) && !atLeastOneRepositoryProcessed) {
            throw new MojoExecutionException("No repository could match your requirements");
        }
    }

    protected List<String> listRepositorySpecifications(Repository repository) throws Exception {
        if (StringUtils.isAnyEmpty(repository.getProjectName(), repository.getSystemUnderTest())) {
            throw new MojoFailureException("Neither the projectName nor the systemUnderTest should be null. " +
                    "Found: projectName="+ repository.getProjectName() + " ,systemUnderTest="+repository.getSystemUnderTest());
        }
        printRepositoryName(repository);


        File indexFile = getIndexFileForRepository(repository);
        if (!indexFile.exists() || refresh) {
            DocumentNode documentHierarchy = repository.retrieveDocumentHierarchy();
            PrintWriter byteArrayWriter = new PrintWriter(tempOutput);
            int i = 1;
            for (DocumentNode node : DocumentNode.traverser.preOrderTraversal(documentHierarchy)) {
                if (node.isExecutable()) {
                    byteArrayWriter.println(format("  [%04d] - [%11s] - [%s]",
                            i, !node.canBeImplemented() ? "implemented" : "", node.getTitle()));
                    i++;
                }
            }
            byteArrayWriter.flush();
            updateIndexFile(indexFile);
        } else {
            System.out.println(format("\tUsing index file '%s'.\n" +
                    "\tYou can force a refresh by removing it or by using '-Dgreenpepper.refresh=true'.", indexFile.getName()));
            System.out.println();
        }

        if (isNotEmpty(specFilter)) {
            System.out.println(format("\tFiltering the specifications using '%s'", specFilter));
            System.out.println();
        }

        ArrayList<String> specifications = new ArrayList<String>();
        for (String line : readLines(new FileInputStream(indexFile))) {
            String specification = decideForLine(line, writer);
            if (isNotBlank(specification)) {
                specifications.add(specification);
            }
        }
        writer.flush();
        System.out.println();
        return specifications;
    }

    private String decideForLine(String line, PrintWriter writer) throws MojoExecutionException {
        boolean lineMatchingReq = false;
        if (isNotEmpty(specFilter)) {
            Matcher matcher = filterPattern.matcher(specFilter);
            if (matcher.matches()) {
                String isImplemented = matcher.group(1);
                String isRegEx = matcher.group(2);
                String searchStr = matcher.group(3);
                boolean matchesImplemented = StringUtils.equals("[I]", isImplemented) && line.contains(" [implemented] ");
                boolean matchesNonImplemented = StringUtils.equals("[!I]", isImplemented) && line.contains(" [           ] ");

                boolean matchesImplementedReq = isEmpty(isImplemented) || (matchesImplemented || matchesNonImplemented);
                boolean matchesSearchStringReq = isNotEmpty(isRegEx) || isEmpty(searchStr) || (isNotEmpty(searchStr) && containsIgnoreCase(line, searchStr));
                boolean matchesRegexStringReq = isEmpty(isRegEx) || (isNotEmpty(isRegEx) && Pattern.matches(searchStr, line));

                if (matchesImplementedReq && matchesRegexStringReq && matchesSearchStringReq) {
                    writer.println(line);
                    lineMatchingReq = true;
                }
            }
        } else {
            writer.println(line);
            lineMatchingReq = true;
        }
        if (lineMatchingReq) {
            Matcher matcher = INDEX_LINE_PATTERN.matcher(line);
            if (matcher.matches()) {
                return matcher.group(1);
            } else {
                throw new MojoExecutionException(format("Corrupted index line (not matching the pattern %s): |%s|", INDEX_LINE_PATTERN.pattern(), line));
            }
        } else {
            return null;
        }
    }

    private void updateIndexFile(File indexFile) throws IOException, NoSuchAlgorithmException {
        writeByteArrayToFile(indexFile, tempOutput.toByteArray());
        tempOutput = new ByteArrayOutputStream();
    }

    private File getIndexFileForRepository(Repository repository) throws UnsupportedEncodingException {
        String indentifiers = repository.getProjectName() + repository.getSystemUnderTest() + repository.getType() + repository.getRoot();
        String indexFilename = format("%s-%s.index", repository.getName() , DigestUtils.md5Hex(indentifiers.getBytes("UTF-8")));
        return new File(specOutputDirectory, indexFilename);
    }

    private void printRepositoryName(Repository repository) {
        System.out.println();
        System.out.println(format(" Repository : %s  (project='%s',sut='%s')",
                repository.getName(), repository.getProjectName(), repository.getSystemUnderTest()));
        System.out.println(StringUtils.repeat(" =",40));
        System.out.println();
    }

    /**
     * <p>addRepository.</p>
     *
     * @param repository a {@link com.greenpepper.maven.plugin.Repository} object.
     */
    void addRepository(Repository repository) {
        repositories.add(repository);
    }

    private void printBanner() {
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println(" G R E E N  P E P P E R  S P E C I F I C A T I O N S   L I S T ");
        System.out.println("----------------------------------------------------------------");
        System.out.println();
    }

    void setPrintWriter(PrintWriter writer) {
        this.writer = writer;
    }

}
