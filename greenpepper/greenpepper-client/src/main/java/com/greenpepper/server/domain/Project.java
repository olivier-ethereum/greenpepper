package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.PROJECT_NAME_IDX;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;

/**
 * Project Class.
 * Definition of a project.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="PROJECT")
@SuppressWarnings("serial")
public class Project extends AbstractVersionedEntity implements Comparable
{
    private String name;
    private Set<Repository> repositories = new HashSet<Repository>();
    private Set<SystemUnderTest> systemUnderTests = new HashSet<SystemUnderTest>();

    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Project} object.
     */
    public static Project newInstance(String name)
    {
        Project project = new Project();
        project.setName(name);
        return project;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length=255)
    public String getName()
    {
        return name;
    }

    /**
     * <p>Getter for the field <code>repositories</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="project", cascade=CascadeType.ALL)
    public Set<Repository> getRepositories()
    {
        return repositories;
    }

    /**
     * <p>Getter for the field <code>systemUnderTests</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="project", cascade=CascadeType.ALL)
    public Set<SystemUnderTest> getSystemUnderTests()
    {
        return systemUnderTests;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * <p>Setter for the field <code>repositories</code>.</p>
     *
     * @param repositories a {@link java.util.Set} object.
     */
    public void setRepositories(Set<Repository> repositories)
    {
        this.repositories = repositories;
    }

    /**
     * <p>Setter for the field <code>systemUnderTests</code>.</p>
     *
     * @param systemUnderTests a {@link java.util.Set} object.
     */
    public void setSystemUnderTests(Set<SystemUnderTest> systemUnderTests)
    {
        this.systemUnderTests = systemUnderTests;
    }

    /**
     * <p>addRepository.</p>
     *
     * @param repo a {@link com.greenpepper.server.domain.Repository} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void addRepository(Repository repo) throws GreenPepperServerException
    {
        if (findRepositoryByName(repo.getName()) != null)
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REPOSITORY_ALREADY_EXISTS, "Repository already exists");
        }

        repo.setProject(this);
        repositories.add(repo);
    }

    /**
     * <p>removeRepository.</p>
     *
     * @param repo a {@link com.greenpepper.server.domain.Repository} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRepository(Repository repo) throws GreenPepperServerException
    {
        if(!repositories.contains(repo))
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REPOSITORY_NOT_FOUND, "Repository not found");
        }

        repositories.remove(repo);
        repo.setProject(null);
    }

    /**
     * <p>addSystemUnderTest.</p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void addSystemUnderTest(SystemUnderTest sut) throws GreenPepperServerException
    {
        if (findSystemUnderTestByName(sut.getName()) != null)
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SUT_ALREADY_EXISTS, "Sut name already exists");
        }

        if(systemUnderTests.isEmpty()){ sut.setIsDefault(true); }
        systemUnderTests.add(sut);
        sut.setProject(this);
    }

    /**
     * <p>removeSystemUnderTest.</p>
     *
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeSystemUnderTest(SystemUnderTest sut) throws GreenPepperServerException
    {
        if(!systemUnderTests.contains(sut))
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SUT_NOT_FOUND, "Sut not found");
        }

        systemUnderTests.remove(sut);
        if(sut.isDefault() && !systemUnderTests.isEmpty())
        {
            systemUnderTests.iterator().next().setIsDefault(true);
        }

        sut.setProject(null);
    }

    /**
     * <p>getDefaultSystemUnderTest.</p>
     *
     * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    @Transient
    public SystemUnderTest getDefaultSystemUnderTest()
    {
        for(SystemUnderTest sut : systemUnderTests)
        {
            if(sut.isDefault())
            {
                return sut;
            }
        }

        return null;
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(PROJECT_NAME_IDX, name);
        return parameters;
    }

    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return getName().compareTo(((Project)o).getName());
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Project))
        {
            return false;
        }

        Project projectCompared = (Project)o;
        if(getName().equals(projectCompared.getName()))
        {
            return true;
        }

        return false;
    }

    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return getName().hashCode();
    }

    private Repository findRepositoryByName(String repoName)
    {
        for (Repository repository : repositories)
        {
            if (repository.getName().equalsIgnoreCase(repoName))
            {
                return repository;
            }
        }
        return null;
    }

    private SystemUnderTest findSystemUnderTestByName(String sutName)
    {
        for (SystemUnderTest sut : systemUnderTests)
        {
            if (sut.getName().equalsIgnoreCase(sutName))
            {
                return sut;
            }
        }
        return null;
    }
}
