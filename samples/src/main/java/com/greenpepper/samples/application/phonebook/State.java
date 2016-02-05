/*
 * Copyright (c) 2007 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.samples.application.phonebook;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
/**
 * <p>State class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="STATE")
@SuppressWarnings("serial")
public class State extends AbstractEntity implements Comparable
{
	private Country country;
	private String name;
	private String code;
    
    /**
     * <p>Constructor for State.</p>
     *
     * @param country a {@link com.greenpepper.samples.application.phonebook.Country} object.
     * @param name a {@link java.lang.String} object.
     * @param code a {@link java.lang.String} object.
     */
    public State(Country country, String name, String code)
    {
        super();
        
    	this.country = country;
        this.name = name;
        this.code = code;
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "CODE", unique = true, nullable = false, length=255)
	public String getCode() 
	{
		return code;
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
     * <p>Getter for the field <code>country</code>.</p>
     *
     * @return a {@link com.greenpepper.samples.application.phonebook.Country} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="COUNTRY_ID")
	public Country getCountry() 
	{
		return country;
	}

	/**
	 * <p>Setter for the field <code>code</code>.</p>
	 *
	 * @param code a {@link java.lang.String} object.
	 */
	public void setCode(String code)
	{
		this.code = code;
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
	 * <p>Setter for the field <code>country</code>.</p>
	 *
	 * @param country a {@link com.greenpepper.samples.application.phonebook.Country} object.
	 */
	public void setCountry(Country country) 
	{
		this.country = country;
	}

    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return name.compareTo(((State)o).name);
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(!(o instanceof State))
        {
            return false;
        }

        State stateCompared = (State)o;
        if(getName().equals(stateCompared.getName()) && 
           getCode().equals(stateCompared.getCode()) &&
           getCountry().equals(stateCompared.getCountry()))
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
    	return getName().hashCode() + getCode().hashCode() + getCountry().hashCode();
    }
}
