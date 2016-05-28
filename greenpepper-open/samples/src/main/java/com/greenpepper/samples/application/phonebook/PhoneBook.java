package com.greenpepper.samples.application.phonebook;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
/**
 * <p>PhoneBook class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="PHONEBOOK")
@SuppressWarnings("serial")
public class PhoneBook extends AbstractEntity
{
	private List<PhoneBookEntry> entries = new ArrayList<PhoneBookEntry>();
	private String name;
	
	/**
	 * <p>Constructor for PhoneBook.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public PhoneBook(String name)
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
     * <p>Getter for the field <code>entries</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @OneToMany(mappedBy="phoneBook", cascade=CascadeType.ALL)
	public List<PhoneBookEntry> getEntries() 
    {
		return entries;
	}

	/**
	 * <p>Setter for the field <code>entries</code>.</p>
	 *
	 * @param entries a {@link java.util.List} object.
	 */
	public void setEntries(List<PhoneBookEntry> entries) 
    {
		this.entries = entries;
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
	 * <p>insert.</p>
	 *
	 * @param firstName a {@link java.lang.String} object.
	 * @param lastName a {@link java.lang.String} object.
	 * @param number a {@link java.lang.String} object.
	 */
	public void insert(String firstName, String lastName, String number)
	{
		entries.add(new PhoneBookEntry(this, firstName, lastName, number));
	}
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(!(o instanceof PhoneBook))
        {
            return false;
        }

        PhoneBook phoneBookCompared = (PhoneBook)o;
        if(getName().equals(phoneBookCompared.getName()))
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
