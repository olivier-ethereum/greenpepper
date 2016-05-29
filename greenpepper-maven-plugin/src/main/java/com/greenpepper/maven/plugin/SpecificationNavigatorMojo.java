package com.greenpepper.maven.plugin;

import com.greenpepper.repository.RepositoryException;
import com.greenpepper.server.domain.DocumentNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.ArrayList;

/**
 * @goal tree
 * @requiresDependencyResolution test
 * @description List the Specifications from the configured repository.
 *
 */
public class SpecificationNavigatorMojo extends AbstractMojo {

    /**
     * @parameter expression="${greenpepper.repositories}"
     * @required
     */
    ArrayList<Repository> repositories;

    public SpecificationNavigatorMojo() {
        this.repositories = new ArrayList<Repository>();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        printBanner();
        processAllRepositories();
    }

    private void processAllRepositories() throws MojoExecutionException {
        try {
            for (Repository repository : repositories) {
                processRepository(repository);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error running the Goal", e);
        }
    }

    private void processRepository(Repository repository) throws Exception {
        if (StringUtils.isAnyEmpty(repository.getProjectName(), repository.getSystemUnderTest())) {
            throw new MojoFailureException("Neither the projectName nor the systemUnderTest should be null. " +
                    "Found: projectName="+ repository.getProjectName() + " ,systemUnderTest="+repository.getSystemUnderTest());
        }
        printRepositoryName(repository);

        DocumentNode documentHierarchy = repository.retrieveDocumentHierarchy();
        int i = 1;
        for (DocumentNode node : DocumentNode.traverser.preOrderTraversal(documentHierarchy)) {
            if (node.isExecutable()) {
                System.out.println(String.format("  [%04d] - [%11s] - [%s]",
                        i, !node.canBeImplemented() ? "implemented" : "", node.getTitle()));
                i++;
            }
        }
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

}
