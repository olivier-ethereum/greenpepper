package com.greenpepper.maven.plugin;

import com.greenpepper.server.domain.DocumentNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static java.lang.String.format;

/**
 * @goal tree
 * @requiresDependencyResolution test
 * @description List the Specifications from the configured repository.
 *
 */
public class SpecificationNavigatorMojo extends AbstractMojo {

    /**
     * @parameter property="greenpepper.repositories"
     * @required
     */
    ArrayList<Repository> repositories;

    /**
     * Set this to a Repository name defined in the pom.xml.
     * This option is only used in case <code>-Dgp.test</code> is used.
     * @parameter property="gp.repo"
     */
    String selectedRepository;

    /**
     * The directory where compiled fixture classes go.
     *
     * @parameter expression="${project.build.directory}/greenpepper"
     * @required
     */
    File specOutputDirectory;


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

    private void processAllRepositories() throws MojoExecutionException {
        if (repositories.isEmpty()) {
            throw new MojoExecutionException("No repository found in your pom configuration");
        }
        boolean atLeastOneRepositoryProcessed = false;
        try {
            for (Repository repository : repositories) {
                if (StringUtils.isNotEmpty(selectedRepository)) {
                    if (StringUtils.equals(selectedRepository, repository.getName())) {
                        processRepository(repository);
                        atLeastOneRepositoryProcessed = true;
                        break;
                    } else {
                        getLog().debug(format("Skipping repository '%s', selected is '%s' ", repository.getName(), selectedRepository));
                    }
                } else {
                    processRepository(repository);
                    atLeastOneRepositoryProcessed = true;
                }
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error running the Goal", e);
        }
        if (!atLeastOneRepositoryProcessed) {
            throw new MojoExecutionException("No repository could match your requirements");
        }
    }

    private void processRepository(Repository repository) throws Exception {
        if (StringUtils.isAnyEmpty(repository.getProjectName(), repository.getSystemUnderTest())) {
            throw new MojoFailureException("Neither the projectName nor the systemUnderTest should be null. " +
                    "Found: projectName="+ repository.getProjectName() + " ,systemUnderTest="+repository.getSystemUnderTest());
        }
        printRepositoryName(repository);

        File indexFile = getIndexFileForRepository(repository);
        if (indexFile.exists()) {
            System.out.println(format("\tUsing index file '%s'.\n" +
                                      "\tYou can force a refresh by removing it.", indexFile.getName()));
            System.out.println();
            IOUtils.copy(new FileInputStream(indexFile), System.out);
        } else {
            DocumentNode documentHierarchy = repository.retrieveDocumentHierarchy();
            int i = 1;
            for (DocumentNode node : DocumentNode.traverser.preOrderTraversal(documentHierarchy)) {
                if (node.isExecutable()) {
                    writer.println(format("  [%04d] - [%11s] - [%s]",
                            i, !node.canBeImplemented() ? "implemented" : "", node.getTitle()));
                    i++;
                }
            }
            writer.flush();
            updateIndexFile(repository);
        }
    }

    private void updateIndexFile(Repository repository) throws IOException, NoSuchAlgorithmException {
        File indexFile = getIndexFileForRepository(repository);
        FileUtils.writeByteArrayToFile(indexFile, tempOutput.toByteArray());
        tempOutput.reset();
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
    public void addRepository(Repository repository) {
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
