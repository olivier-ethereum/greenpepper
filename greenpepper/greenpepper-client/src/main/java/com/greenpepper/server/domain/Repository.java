package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_BASEREPO_URL_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_BASETEST_URL_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_BASE_URL_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_CONTENTTYPE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_MAX_USERS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_NAME_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_PASSWORD_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_PROJECT_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_UID_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_USERNAME_IDX;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.runner.FactoryConverter;
import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.component.ContentType;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;


/**
 * Repository Class.
 * Definition of the repository.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="REPOSITORY", uniqueConstraints = {@UniqueConstraint(columnNames={"NAME", "PROJECT_ID"})})
@SuppressWarnings("serial")
public class Repository extends AbstractVersionedEntity implements Comparable
{
    private String name;
    private String uid;
    private String baseUrl;
    private String repositoryBaseUrl;
    private String testBaseUrl;
    private ContentType contentType = ContentType.UNKNOWN;
    private String username;
    private String password;

    private Project project;
    private RepositoryType type;
    private Set<Requirement> requirements = new HashSet<Requirement>();
    private Set<Specification> specifications = new HashSet<Specification>();
    private int maxUsers;

    /**
     * <p>newInstance.</p>
     *
     * @param uid a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Repository} object.
     */
    public static Repository newInstance(String uid)
    {
        Repository repository = new Repository();
        repository.setUid(uid);
        return repository;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "NAME", nullable = false, length=255)
    public String getName()
    {
        return this.name;
    }

    /**
     * <p>Getter for the field <code>uid</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "UIDENT", unique = true, nullable = false, length=255)
    public String getUid()
    {
        return this.uid;
    }

    /**
     * <p>Getter for the field <code>baseUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "BASE_URL", nullable = false, length=255)
    public String getBaseUrl()
    {
        return this.baseUrl;
    }

    /**
     * <p>getBaseRepositoryUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "BASE_REPOSITORY_URL", nullable = false, length=255)
    public String getBaseRepositoryUrl()
    {
        return this.repositoryBaseUrl;
    }

    /**
     * <p>getBaseTestUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "BASE_TEST_URL", nullable = false, length=255)
    public String getBaseTestUrl()
    {
        return this.testBaseUrl;
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "USERNAME", nullable = true, length=15)
	public String getUsername()
	{
		return username;
	}

    /**
     * <p>Getter for the field <code>password</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "PASSWORD", nullable = true, length=15)
	public String getPassword()
	{
		return password;
	}

    /**
     * <p>Getter for the field <code>project</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Project} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="PROJECT_ID")
    public Project getProject()
    {
        return project;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.RepositoryType} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="REPOSITORY_TYPE_ID")
    public RepositoryType getType()
    {
        return this.type;
    }

    /**
     * <p>Getter for the field <code>requirements</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="repository", cascade=CascadeType.ALL)
    public Set<Requirement> getRequirements()
    {
        return this.requirements;
    }

    /**
     * <p>Getter for the field <code>specifications</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="repository", cascade=CascadeType.ALL)
    public Set<Specification> getSpecifications()
    {
        return specifications;
    }

    /**
     * <p>Getter for the field <code>contentType</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.component.ContentType} object.
     */
    public ContentType getContentType()
    {
        return contentType;
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
     * <p>Setter for the field <code>uid</code>.</p>
     *
     * @param uid a {@link java.lang.String} object.
     */
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    /**
     * <p>Setter for the field <code>baseUrl</code>.</p>
     *
     * @param baseUrl a {@link java.lang.String} object.
     */
    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    /**
     * <p>setBaseRepositoryUrl.</p>
     *
     * @param repositoryBaseUrl a {@link java.lang.String} object.
     */
    public void setBaseRepositoryUrl(String repositoryBaseUrl)
    {
        this.repositoryBaseUrl = repositoryBaseUrl;
    }

    /**
     * <p>setBaseTestUrl.</p>
     *
     * @param testBaseUrl a {@link java.lang.String} object.
     */
    public void setBaseTestUrl(String testBaseUrl)
    {
        this.testBaseUrl = testBaseUrl;
    }

	/**
	 * <p>Setter for the field <code>username</code>.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 */
	public void setUsername(String username) 
	{
		this.username = username;
	}

	/**
	 * <p>Setter for the field <code>password</code>.</p>
	 *
	 * @param password a {@link java.lang.String} object.
	 */
	public void setPassword(String password) 
	{
		this.password = password;
	}

    /**
     * <p>Setter for the field <code>contentType</code>.</p>
     *
     * @param contentType a {@link com.greenpepper.server.domain.component.ContentType} object.
     */
    public void setContentType(ContentType contentType)
    {
        this.contentType = contentType;
    }

    /**
     * <p>Setter for the field <code>project</code>.</p>
     *
     * @param project a {@link com.greenpepper.server.domain.Project} object.
     */
    public void setProject(Project project)
    {
        this.project = project;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link com.greenpepper.server.domain.RepositoryType} object.
     */
    public void setType(RepositoryType type)
    {
        this.type = type;
    }

    /**
     * <p>Setter for the field <code>requirements</code>.</p>
     *
     * @param requirements a {@link java.util.Set} object.
     */
    public void setRequirements(Set<Requirement> requirements)
    {
        this.requirements = requirements;
    }

    /**
     * <p>Setter for the field <code>specifications</code>.</p>
     *
     * @param specifications a {@link java.util.Set} object.
     */
    public void setSpecifications(Set<Specification> specifications)
    {
        this.specifications = specifications;
    }

    /**
     * <p>addRequirement.</p>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void addRequirement(Requirement requirement) throws GreenPepperServerException
    {
        if(requirements.contains(requirement) || requirementNameExists(requirement.getName()))
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REQUIREMENT_ALREADY_EXISTS, "Requirement already exists");

        requirement.setRepository(this);
        requirements.add(requirement);
    }

    /**
     * <p>removeRequirement.</p>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeRequirement(Requirement requirement) throws GreenPepperServerException
    {
        if(!requirements.contains(requirement) )
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REQUIREMENT_NOT_FOUND, "Requirement not found");

        requirements.remove(requirement);
        requirement.setRepository(null);
    }

    /**
     * <p>addSpecification.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void addSpecification(Specification specification) throws GreenPepperServerException
    {
        if(specifications.contains(specification) || specificationNameExists(specification.getName()))
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SPECIFICATION_ALREADY_EXISTS, "Specification already exists");

        specification.setRepository(this);
        specifications.add(specification);
    }

    /**
     * <p>removeSpecification.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeSpecification(Specification specification) throws GreenPepperServerException
    {
        if(!specifications.contains(specification))
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SPECIFICATION_NOT_FOUND, "Specification not found");

        specifications.remove(specification);
        specification.setRepository(null);
    }

    /**
     * <p>Getter for the field <code>maxUsers</code>.</p>
     *
     * @return a int.
     */
    @Transient
    public int getMaxUsers()
    {
        return maxUsers;
    }

    /**
     * <p>Setter for the field <code>maxUsers</code>.</p>
     *
     * @param maxUsers a int.
     */
    public void setMaxUsers(int maxUsers)
    {
        this.maxUsers = maxUsers;
    }

    /**
     * <p>resolveName.</p>
     *
     * @param document a {@link com.greenpepper.server.domain.Document} object.
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public String resolveName(Document document) throws GreenPepperServerException
    {
        return type.resolveName(document);
    }

	/**
	 * <p>asCmdLineOption.</p>
	 *
	 * @param env a {@link com.greenpepper.server.domain.EnvironmentType} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String asCmdLineOption(EnvironmentType env) 
	{
		return type.asFactoryArguments(this, env, false, null, null);
	}

	/**
	 * <p>asDocumentRepository.</p>
	 *
	 * @param env a {@link com.greenpepper.server.domain.EnvironmentType} object.
	 * @return a {@link com.greenpepper.repository.DocumentRepository} object.
	 * @throws java.lang.Exception if any.
	 */
	public DocumentRepository asDocumentRepository(EnvironmentType env) throws Exception 
	{
	    return asDocumentRepository(env, null, null);
	}

	/**
	 * <p>asDocumentRepository.</p>
	 *
	 * @param env a {@link com.greenpepper.server.domain.EnvironmentType} object.
	 * @param user a {@link java.lang.String} object.
	 * @param pwd a {@link java.lang.String} object.
	 * @return a {@link com.greenpepper.repository.DocumentRepository} object.
	 * @throws java.lang.Exception if any.
	 */
	public DocumentRepository asDocumentRepository(EnvironmentType env, String user, String pwd) throws Exception 
	{
	    return (DocumentRepository)new FactoryConverter().convert(type.asFactoryArguments(this, env, true, user, pwd));
	}

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(REPOSITORY_NAME_IDX, XmlRpcDataMarshaller.padNull(name));
        parameters.add(REPOSITORY_UID_IDX, XmlRpcDataMarshaller.padNull(uid));
        parameters.add(REPOSITORY_PROJECT_IDX, project != null ? project.marshallize() : Project.newInstance("").marshallize());
        parameters.add(REPOSITORY_TYPE_IDX, type != null ? type.marshallize() : RepositoryType.newInstance("").marshallize());
        parameters.add(REPOSITORY_CONTENTTYPE_IDX, contentType.toString());
        parameters.add(REPOSITORY_BASE_URL_IDX, XmlRpcDataMarshaller.padNull(getBaseUrl()));
        parameters.add(REPOSITORY_BASEREPO_URL_IDX, XmlRpcDataMarshaller.padNull(getBaseRepositoryUrl()));
        parameters.add(REPOSITORY_BASETEST_URL_IDX, XmlRpcDataMarshaller.padNull(getBaseTestUrl()));
        parameters.add(REPOSITORY_USERNAME_IDX, XmlRpcDataMarshaller.padNull(username));
        parameters.add(REPOSITORY_PASSWORD_IDX, XmlRpcDataMarshaller.padNull(password));
        parameters.add(REPOSITORY_MAX_USERS_IDX, maxUsers);
        return parameters;
    }

    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return getName().compareTo(((Repository)o).getName());
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Repository))
        {
            return false;
        }

        Repository repoCompared = (Repository)o;
        return getUid().equals(repoCompared.getUid());
    }

    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return getUid() == null ? 0 : getUid().hashCode();
    }

    private boolean requirementNameExists(String requirementName)
    {
        for (Requirement requirement : requirements)
            if (requirement.getName().equalsIgnoreCase(requirementName))
                return true;

        return false;
    }

    private boolean specificationNameExists(String specificationName)
    {
        for (Specification specification : specifications)
            if (specification.getName().equalsIgnoreCase(specificationName))
                return true;

        return false;
    }
}
