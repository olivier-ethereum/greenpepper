package com.greenpepper.maven.plugin;

import com.greenpepper.server.domain.DocumentNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.PrintWriter;
import java.util.ArrayList;

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
    private PrintWriter writer;

    public SpecificationNavigatorMojo() {
        this.repositories = new ArrayList<Repository>();
        this.writer = new PrintWriter(System.out);
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
        if ( StringUtils.isNotEmpty(selectedRepository)
                && !StringUtils.equals(selectedRepository,repository.getName())) {
            getLog().debug(String.format("Skipping repository '%s', selected is '%s' ", repository.getName(), selectedRepository));
            return;
        }
        if (StringUtils.isAnyEmpty(repository.getProjectName(), repository.getSystemUnderTest())) {
            throw new MojoFailureException("Neither the projectName nor the systemUnderTest should be null. " +
                    "Found: projectName="+ repository.getProjectName() + " ,systemUnderTest="+repository.getSystemUnderTest());
        }
        printRepositoryName(repository);

        DocumentNode documentHierarchy = repository.retrieveDocumentHierarchy();
        int i = 1;
        for (DocumentNode node : DocumentNode.traverser.preOrderTraversal(documentHierarchy)) {
            if (node.isExecutable()) {
                writer.println(String.format("  [%04d] - [%11s] - [%s]",
                        i, !node.canBeImplemented() ? "implemented" : "", node.getTitle()));
                i++;
            }
        }
        writer.flush();
    }

    private void printRepositoryName(Repository repository) {
        System.out.println();
        System.out.println(String.format(" Repository : %s  (project='%s',sut='%s')",
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
