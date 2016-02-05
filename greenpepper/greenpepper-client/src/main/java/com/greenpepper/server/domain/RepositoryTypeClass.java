package com.greenpepper.server.domain;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
/**
 * <p>RepositoryTypeClass class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="REPOSITORY_TYPE_CLASS", uniqueConstraints = {@UniqueConstraint(columnNames={"ENVIRONMENT_TYPE_ID", "REPOSITORY_TYPE_ID"})})
@SuppressWarnings("serial")
public class RepositoryTypeClass extends AbstractEntity
{
	private String className;
	private EnvironmentType envType;
	private RepositoryType repositoryType;

	/**
	 * <p>newInstance.</p>
	 *
	 * @param repositoryType a {@link com.greenpepper.server.domain.RepositoryType} object.
	 * @param envType a {@link com.greenpepper.server.domain.EnvironmentType} object.
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link com.greenpepper.server.domain.RepositoryTypeClass} object.
	 */
	public static RepositoryTypeClass newInstance(RepositoryType repositoryType, EnvironmentType envType, String className)
	{
		RepositoryTypeClass repoTypeClass = new RepositoryTypeClass();
		repoTypeClass.setRepositoryType(repositoryType);
		repoTypeClass.setEnvType(envType);
		repoTypeClass.setClassName(className);
		
		return repoTypeClass;
	}
	
    /**
     * <p>Getter for the field <code>className</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "CLASSNAME", nullable = false, length=255)
	public String getClassName() 
	{
		return className;
	}
	
    /**
     * <p>Getter for the field <code>envType</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.EnvironmentType} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="ENVIRONMENT_TYPE_ID", nullable = false)
	public EnvironmentType getEnvType() 
	{
		return envType;
	}
	
    /**
     * <p>Getter for the field <code>repositoryType</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.RepositoryType} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="REPOSITORY_TYPE_ID", nullable = false)
	public RepositoryType getRepositoryType() 
	{
		return repositoryType;
	}
	
	/**
	 * <p>Setter for the field <code>className</code>.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}
	
	/**
	 * <p>Setter for the field <code>envType</code>.</p>
	 *
	 * @param envType a {@link com.greenpepper.server.domain.EnvironmentType} object.
	 */
	public void setEnvType(EnvironmentType envType)
	{
		this.envType = envType;
	}
	
	/**
	 * <p>Setter for the field <code>repositoryType</code>.</p>
	 *
	 * @param repositoryType a {@link com.greenpepper.server.domain.RepositoryType} object.
	 */
	public void setRepositoryType(RepositoryType repositoryType) 
	{
		this.repositoryType = repositoryType;
	}

	/**
	 * <p>marshallize.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> marshallize() 
	{
		return null;
	}
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof RepositoryTypeClass))
        {
            return false;
        }
        
        RepositoryTypeClass typeCompared = (RepositoryTypeClass)o;
        if(getClassName().equals(typeCompared.getClassName()) &&
    	   getEnvType().equals(typeCompared.getEnvType()) &&
    	   getRepositoryType().equals(typeCompared.getRepositoryType()))
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
        return getClassName().hashCode() << 1 + getEnvType().hashCode() << 1 + getRepositoryType().hashCode() << 1;
    }
}
