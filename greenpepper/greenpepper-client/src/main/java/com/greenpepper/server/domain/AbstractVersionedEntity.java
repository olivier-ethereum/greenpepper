package com.greenpepper.server.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
/**
 * <p>Abstract AbstractVersionedEntity class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractVersionedEntity extends AbstractEntity
{
    private Integer version;
    
    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    @Version
    @Column(name="VERSION")
    public Integer getVersion()
    {
        return this.version;
    }

    /**
     * <p>Setter for the field <code>version</code>.</p>
     *
     * @param version a {@link java.lang.Integer} object.
     */
    public void setVersion(Integer version)
    {
        this.version = version;
    }
}
