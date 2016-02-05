package com.greenpepper.server.domain;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;


@Entity
/**
 * <p>SystemInfo class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="SYSTEM_INFO")
@SuppressWarnings("serial")
public class SystemInfo extends AbstractUniqueEntity
{
    private String license;
    private String gpVersion; 

    /**
     * <p>Getter for the field <code>license</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Lob
    @Column(name = "LICENSE", nullable = true, length = 4096)
    public String getLicense()
    {
        return license;
    }

    /**
     * <p>Getter for the field <code>gpVersion</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "GPVERSION")
    public String getGpVersion()
    {
    	return gpVersion;
    }

    /**
     * <p>Setter for the field <code>license</code>.</p>
     *
     * @param license a {@link java.lang.String} object.
     */
    public void setLicense(String license)
    {
        this.license = license;
    }
    
    /**
     * <p>Setter for the field <code>gpVersion</code>.</p>
     *
     * @param gpVersion a {@link java.lang.String} object.
     */
    public void setGpVersion(String gpVersion)
    {
        this.gpVersion = gpVersion;
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
    	return new Vector<Object>();
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if (super.equals(o))
        {
            return o instanceof SystemInfo;
        }

        return false;
    }
}
