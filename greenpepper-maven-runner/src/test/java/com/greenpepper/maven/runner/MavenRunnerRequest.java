package com.greenpepper.maven.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.maven.artifact.repository.ArtifactRepository;

public class MavenRunnerRequest {
    

    private CommandLine commandLine;
    private String workingDirectory;
    private Properties userProperties;
    private Properties systemProperties;
    private List<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
    private String localRepository;
    private List<ArtifactRepository> pluginRepositories = new ArrayList<ArtifactRepository>();
    

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Properties getUserProperties() {
        return userProperties;
    }

    public Properties getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public void setUserProperties(Properties userProperties) {
        this.userProperties = userProperties;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public void setLocalRepositoryPath(String localRepository) {
        this.setLocalRepository(localRepository);
        
    }

    public void addRemoteRepository(ArtifactRepository buildArtifactRepository) {
        this.remoteRepositories.add(buildArtifactRepository);
        
    }

    public void addPluginArtifactRepository(ArtifactRepository buildArtifactRepository) {
        this.pluginRepositories.add(buildArtifactRepository);
    }

    public String getLocalRepository() {
        return localRepository;
    }

    public void setLocalRepository(String localRepository) {
        this.localRepository = localRepository;
    }
    
    public List<ArtifactRepository> getRemoteRepositories() {
        return remoteRepositories;
    }



}
