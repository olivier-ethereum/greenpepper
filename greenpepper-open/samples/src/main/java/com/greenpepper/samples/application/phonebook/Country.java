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

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
/**
 * <p>Country class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="COUNTRY")
@SuppressWarnings("serial")
public class Country extends AbstractEntity
{
    private String name;
    private Set<State> states = new TreeSet<State>();

    /**
     * <p>Constructor for Country.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public Country(String name)
    {
        super();
        
    	this.name = name;
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
     * <p>Getter for the field <code>states</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="country", cascade=CascadeType.ALL)
    public Set<State> getStates()
    {
        return states;
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
     * <p>Setter for the field <code>states</code>.</p>
     *
     * @param states a {@link java.util.Set} object.
     */
    public void setStates(Set<State> states)
    {
        this.states = states;
    }
    
    /**
     * <p>addState.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param code a {@link java.lang.String} object.
     */
    public void addState(String name, String code)
    {
    	states.add(new State(this, name, code));
    }
    
    /**
     * <p>removeState.</p>
     *
     * @param state a {@link com.greenpepper.samples.application.phonebook.State} object.
     */
    public void removeState(State state)
    {
    	states.remove(state);
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(!(o instanceof Country))
        {
            return false;
        }

        Country countryCompared = (Country)o;
        if(getName().equals(countryCompared.getName()))
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
    	return getName().hashCode();
    }
}
