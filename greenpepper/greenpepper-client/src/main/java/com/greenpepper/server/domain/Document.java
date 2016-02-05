package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.DOCUMENT_NAME_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.DOCUMENT_REPOSITORY_IDX;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.greenpepper.server.GreenPepperServerException;

/**
 * <p>Abstract Document class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class Document extends AbstractUniqueEntity implements Comparable
{
    private String name;
    private String resolvedName;
    private Repository repository;

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
     * <p>Getter for the field <code>repository</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Repository} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="REPOSITORY_ID")
    public Repository getRepository()
    {
        return this.repository;
    }

    /**
     * For UI purposes
     *
     * @param resolvedName a {@link java.lang.String} object.
     */
    public void setResolvedName(String resolvedName)
    {
        this.resolvedName = resolvedName;
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
     * <p>Setter for the field <code>repository</code>.</p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     */
    public void setRepository(Repository repository)
    {
        this.repository = repository;
    }

//    @Transient
//    public String getResolvedUri() throws GreenPepperServerException
//    {
//        return repository.resolveUri(this);
//    }

    /**
     * <p>Getter for the field <code>resolvedName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    @Transient
    public String getResolvedName() throws GreenPepperServerException
    {
        if(resolvedName != null){return resolvedName;}
        return repository.resolveName(this);
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(DOCUMENT_NAME_IDX, name);
        parameters.add(DOCUMENT_REPOSITORY_IDX, repository.marshallize());
        return parameters;
    }
    
    /**
     * <p>equalsTo.</p>
     *
     * @param o a {@link java.lang.Object} object.
     * @return a boolean.
     */
    public boolean equalsTo(Object o)
    {
        if (o == null || !(o instanceof Document))
        {
            return false;
        }
        
        Document docCompared = (Document)o;
        if(getName() == null || !getName().equals(docCompared.getName())) return false;
        if(getRepository() == null || !getRepository().equals(docCompared.getRepository())) return false;
        
        return true;
    }
    
    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return name.compareTo(((Document)o).name);
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof Document))
        {
            return false;
        }

        return super.equals(o);
    }
}
