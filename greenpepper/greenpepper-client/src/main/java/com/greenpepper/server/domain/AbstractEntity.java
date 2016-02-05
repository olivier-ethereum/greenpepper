package com.greenpepper.server.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


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
public abstract class AbstractEntity implements Serializable, Marshalizable
{
    private Long id;
    
    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    @Id
    @GeneratedValue
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
}
