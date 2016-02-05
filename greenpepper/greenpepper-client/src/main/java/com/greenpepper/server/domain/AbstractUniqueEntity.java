package com.greenpepper.server.domain;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
/**
 * <p>Abstract AbstractUniqueEntity class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractUniqueEntity extends AbstractVersionedEntity
{
    protected String uuid;
    
    /**
     * <p>Constructor for AbstractUniqueEntity.</p>
     */
    public AbstractUniqueEntity()
    {
        uuid = UUID.randomUUID().toString();
    }
    
    /**
     * <p>getUUID.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name="UUID", nullable = false)
    public String getUUID()
    {
        return this.uuid;
    }
    
    /**
     * <p>setUUID.</p>
     *
     * @param uuid a {@link java.lang.String} object.
     */
    public void setUUID(String uuid)
    {
        this.uuid = uuid;
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        return getUUID().equals(((AbstractUniqueEntity)o).getUUID());
    }
    
    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return getUUID() == null ? 0 : getUUID().hashCode();
    }
}
