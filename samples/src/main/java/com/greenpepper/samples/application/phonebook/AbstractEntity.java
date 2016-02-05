package com.greenpepper.samples.application.phonebook;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


/**
 * Abstract Entity Class.
 * All POJOS have to exntend it.
 * Provides the versioning and the Primary key.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@MappedSuperclass
public abstract class AbstractEntity implements Serializable
{
    private Long id;
    private Integer version;
    
    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getId()
    {
        return this.id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.Long} object.
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
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
