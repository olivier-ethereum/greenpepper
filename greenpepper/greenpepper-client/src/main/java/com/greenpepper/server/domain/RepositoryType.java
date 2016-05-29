package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_NAME_FORMAT_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_NAME_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_REPOCLASSES_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_URI_FORMAT_IDX;

import java.net.URI;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import com.greenpepper.util.StringUtil;
import com.greenpepper.util.URIUtil;
import org.apache.commons.lang3.StringUtils;

import static com.greenpepper.util.StringUtil.escapeSemiColon;

/**
 * RepositoryType Class.
 * Known types: JIRA / CONFLUENCE / FILE ...
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="REPOSITORY_TYPE")
@SuppressWarnings("serial")
public class RepositoryType extends AbstractVersionedEntity implements Comparable
{
    private String name;
    private String documentUrlFormat;
    private String testUrlFormat;
    private String repositoryClass;
    private Set<RepositoryTypeClass> repositoryTypeClasses = new HashSet<RepositoryTypeClass>();

    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.RepositoryType} object.
     */
    public static RepositoryType newInstance(String name)
    {
        RepositoryType type = new RepositoryType();
        type.setName(name);
        return type;
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
     * <p>Getter for the field <code>repositoryTypeClasses</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="repositoryType", cascade=CascadeType.ALL)
    public Set<RepositoryTypeClass> getRepositoryTypeClasses()
    {
        return repositoryTypeClasses;
    }

    /**
     * <p>Getter for the field <code>documentUrlFormat</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "DOCUMENT_URL_FORMAT", nullable = true, length=255)
    public String getDocumentUrlFormat()
    {
        return documentUrlFormat;
    }

    /**
     * <p>Getter for the field <code>testUrlFormat</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "TEST_URL_FORMAT", nullable = true, length=255)
    public String getTestUrlFormat()
    {
        return testUrlFormat;
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
     * <p>Setter for the field <code>repositoryTypeClasses</code>.</p>
     *
     * @param repositoryTypeClasses a {@link java.util.Set} object.
     */
    public void setRepositoryTypeClasses(Set<RepositoryTypeClass> repositoryTypeClasses)
    {
        this.repositoryTypeClasses = repositoryTypeClasses;
    }
    
    /**
     * <p>Setter for the field <code>documentUrlFormat</code>.</p>
     *
     * @param documentUrlFormat a {@link java.lang.String} object.
     */
    public void setDocumentUrlFormat(String documentUrlFormat)
    {
        this.documentUrlFormat = StringUtil.toNullIfEmpty(documentUrlFormat);
    }
    
    /**
     * <p>Setter for the field <code>testUrlFormat</code>.</p>
     *
     * @param testUrlFormat a {@link java.lang.String} object.
     */
    public void setTestUrlFormat(String testUrlFormat)
    {
        this.testUrlFormat = StringUtil.toNullIfEmpty(testUrlFormat);
    }

    /**
     * <p>resolveName.</p>
     *
     * @param document a {@link com.greenpepper.server.domain.Document} object.
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public String resolveName(Document document)throws GreenPepperServerException
    {
        if(!StringUtil.isEmpty(documentUrlFormat))
        {
            return String.format(documentUrlFormat, document.getRepository().getBaseRepositoryUrl(), document.getName());
        }
        
        return null;
    }

    /**
     * <p>resolveUri.</p>
     *
     * @param document a {@link com.greenpepper.server.domain.Document} object.
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public String resolveUri(Document document)throws GreenPepperServerException
    {
        if(!StringUtil.isEmpty(testUrlFormat))
        {
            return String.format(testUrlFormat, document.getRepository().getBaseTestUrl(), document.getName());
        }
        
        return null;
    }
    
    /**
     * <p>registerClassForEnvironment.</p>
     *
     * @param className a {@link java.lang.String} object.
     * @param envType a {@link com.greenpepper.server.domain.EnvironmentType} object.
     */
    public void registerClassForEnvironment(String className, EnvironmentType envType)
    {
    	RepositoryTypeClass repoTypeClass = RepositoryTypeClass.newInstance(this, envType, className);
    	repositoryTypeClasses.add(repoTypeClass);
    }
    
    /**
     * <p>getRepositoryTypeClass.</p>
     *
     * @param envType a {@link com.greenpepper.server.domain.EnvironmentType} object.
     * @return a {@link java.lang.String} object.
     */
    @Transient
    public String getRepositoryTypeClass(EnvironmentType envType)
    {
    	for(RepositoryTypeClass repoTypeClass : repositoryTypeClasses)
    		if(repoTypeClass.getEnvType().equals(envType))
    			return repoTypeClass.getClassName();
    	
    	return null;
    }

    /**
     * <p>asFactoryArguments.</p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param env a {@link com.greenpepper.server.domain.EnvironmentType} object.
     * @param withStyle a boolean.
     * @param user a {@link java.lang.String} object.
     * @param pwd a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String asFactoryArguments(Repository repository, EnvironmentType env, boolean withStyle, String user, String pwd) 
	{
    	StringBuilder sb = new StringBuilder();
    	sb.append(getRepositoryTypeClass(env)).append(";");
    	sb.append(withStyle || name.equals("FILE") ? repository.getBaseTestUrl() : withNoStyle(repository.getBaseTestUrl()));

    	if(user == null)
    	{
    		if(!StringUtil.isEmpty(repository.getUsername()))
    			sb.append(";").append(repository.getUsername()).append(";").append(escapeSemiColon(repository.getPassword()));
    	}
    	else
    	{
            sb.append(";").append(user).append(";").append(escapeSemiColon(pwd));
    	}
    	
		return sb.toString();
	}

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(REPOSITORY_TYPE_NAME_IDX, StringUtils.defaultString(name));
        
    	Hashtable<String, String> repoTypeClasses = new Hashtable<String, String>();
    	for(RepositoryTypeClass repoTypeClass : repositoryTypeClasses)
    		repoTypeClasses.put(repoTypeClass.getEnvType().getName(), repoTypeClass.getClassName());
        
        parameters.add(REPOSITORY_TYPE_REPOCLASSES_IDX, repoTypeClasses);
        parameters.add(REPOSITORY_TYPE_NAME_FORMAT_IDX, StringUtils.defaultString(getDocumentUrlFormat()));
        parameters.add(REPOSITORY_TYPE_URI_FORMAT_IDX, StringUtils.defaultString(getTestUrlFormat()));
        return parameters;
    }
    
    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return getName().compareTo(((RepositoryType)o).getName());
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof RepositoryType))
        {
            return false;
        }
        
        RepositoryType typeCompared = (RepositoryType)o;
		return getName().equals(typeCompared.getName());
	}
    
    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return getName() == null ? 0 : getName().hashCode();
    }

    /**
     * <p>Getter for the field <code>repositoryClass</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "REPOSITORY_CLASS", nullable = true, length=255)
    public String getRepositoryClass()
    {
    	return repositoryClass;
    }

    /**
     * <p>Setter for the field <code>repositoryClass</code>.</p>
     *
     * @param repositoryClass a {@link java.lang.String} object.
     */
    public void setRepositoryClass(String repositoryClass)
    {
    	this.repositoryClass = repositoryClass;
    }
    
    private String withNoStyle(String location)
    {
    	URI uri = URI.create(URIUtil.raw(location));
    	StringBuilder sb = new StringBuilder();
    	if(uri.getScheme() != null) sb.append(uri.getScheme()).append("://");
    	if(uri.getAuthority() != null) sb.append(uri.getAuthority());
    	if(uri.getPath() != null) sb.append(uri.getPath());
    	
    	String query = uri.getQuery();
    	if(query == null) query = "?includeStyle=false";
    	else query += "&includeStyle=false";
    	sb.append("?").append(query);
    	
    	if(uri.getFragment() != null) sb.append("#").append(uri.getFragment());
    	
    	return sb.toString();
    }
}
