package com.greenpepper.server.rpc.runner;

import org.junit.Ignore;
import org.junit.Test;

import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.SystemUnderTest;

public class XmlRpcRemoteRunnerTest {


    private XmlRpcRemoteRunner xmlRpcRemoteRunner;

    /**
     * Test against a confluence installation
     * @throws Exception
     */
    @Test
    @Ignore
    public void testGetSpecificationHierarchy() throws Exception {
        xmlRpcRemoteRunner = new XmlRpcRemoteRunner("http://localhost:8080/", "greenpepper1");
        Repository repository = new Repository();
        repository.setName("GREENPEPPERDEMO");
        repository.setUid("Confluence-GREENPEPPERDEMO");
        SystemUnderTest systemUnderTest =  new SystemUnderTest();
        systemUnderTest.setName("Demo - PhoneBook");
        Project project = new Project();
        project.addRepository(repository);
        project.setName("GreenPepper Demo");
        systemUnderTest.setProject(project);
        
        xmlRpcRemoteRunner.getSpecificationHierarchy(repository, systemUnderTest);
        
        
    }

    @Test
    public void testGetSpecificationHierarchyConfluence() throws Exception {
        xmlRpcRemoteRunner = new XmlRpcRemoteRunner("http://cf32.qua1.strator.eu/", "greenpepper1");
        Repository repository = new Repository();
        repository.setName("TestsGPVenteEncaissement");
        repository.setUid("Confluence-TestsGPVenteEncaissement");
        SystemUnderTest systemUnderTest =  new SystemUnderTest();
        systemUnderTest.setName("Vente 2.14");
        Project project = new Project();
        project.addRepository(repository);
        project.setName("VenteEncaissement");
        systemUnderTest.setProject(project);
        
        xmlRpcRemoteRunner.getSpecificationHierarchy(repository, systemUnderTest);
        
        
    }
    
}
