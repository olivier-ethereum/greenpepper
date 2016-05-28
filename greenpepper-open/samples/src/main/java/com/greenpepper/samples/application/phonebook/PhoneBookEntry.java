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
 * <p>PhoneBookEntry class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="PHONEBOOK_ENTRY")
@SuppressWarnings("serial")
public class PhoneBookEntry extends AbstractEntity
{	
	private PhoneBook phoneBook;
	private String firstName;
	private String lastName;
	private String number;
	
	/**
	 * <p>Constructor for PhoneBookEntry.</p>
	 *
	 * @param phoneBook a {@link com.greenpepper.samples.application.phonebook.PhoneBook} object.
	 * @param firstName a {@link java.lang.String} object.
	 * @param lastName a {@link java.lang.String} object.
	 * @param number a {@link java.lang.String} object.
	 */
	public PhoneBookEntry(PhoneBook phoneBook, String firstName, String lastName, String number) 
    {
        super();

		this.phoneBook = phoneBook;
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
	}

    /**
     * <p>Getter for the field <code>firstName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "FIRSTNAME", unique = true, nullable = false, length=255)
	public String getFirstName()
	{
		return firstName;
	}

    /**
     * <p>Getter for the field <code>lastName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "LASTNAME", unique = true, nullable = false, length=255)
	public String getLastName() 
	{
		return lastName;
	}

    /**
     * <p>Getter for the field <code>number</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "NUMBER", unique = true, nullable = false, length=255)
	public String getNumber() 
	{
		return number;
	}

    /**
     * <p>Getter for the field <code>phoneBook</code>.</p>
     *
     * @return a {@link com.greenpepper.samples.application.phonebook.PhoneBook} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="PHONEBOOK_ID")
	public PhoneBook getPhoneBook() 
	{
		return phoneBook;
	}

	/**
	 * <p>Setter for the field <code>firstName</code>.</p>
	 *
	 * @param firstName a {@link java.lang.String} object.
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	/**
	 * <p>Setter for the field <code>lastName</code>.</p>
	 *
	 * @param lastName a {@link java.lang.String} object.
	 */
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	/**
	 * <p>Setter for the field <code>number</code>.</p>
	 *
	 * @param number a {@link java.lang.String} object.
	 */
	public void setNumber(String number) 
	{
		this.number = number;
	}

	/**
	 * <p>Setter for the field <code>phoneBook</code>.</p>
	 *
	 * @param phoneBook a {@link com.greenpepper.samples.application.phonebook.PhoneBook} object.
	 */
	public void setPhoneBook(PhoneBook phoneBook)
	{
		this.phoneBook = phoneBook;
	}
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(!(o instanceof PhoneBookEntry))
        {
            return false;
        }

        PhoneBookEntry entryCompared = (PhoneBookEntry)o;
        if(getFirstName().equals(entryCompared.getFirstName()) &&
           getLastName().equals(entryCompared.getLastName()) &&
           getNumber().equals(entryCompared.getNumber()) &&
           getPhoneBook().equals(entryCompared.getPhoneBook()))
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
    	return getFirstName().hashCode() + getLastName().hashCode() + getNumber().hashCode() + getPhoneBook().hashCode();
    }
}
