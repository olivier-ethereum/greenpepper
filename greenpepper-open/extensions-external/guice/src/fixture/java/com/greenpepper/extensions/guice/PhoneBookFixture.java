/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
package com.greenpepper.extensions.guice;

import java.util.List;

import com.google.inject.Inject;
import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.EnterRow;

public class PhoneBookFixture
{
	private PhoneBook phoneBook;
	private PhoneBookEntry contact;

	@Inject
	public PhoneBookFixture(PhoneBook phoneBook)
	{
		this.phoneBook = phoneBook;
	}

	public PhoneBookEntry getContact()
	{
		return contact;
	}

	public void setContact(PhoneBookEntry contact)
	{
		this.contact = contact;
	}

	@CollectionProvider
	public List<PhoneBookEntry> getPhoneBookEntries()
	{
		return phoneBook.getEntries();
	}

	@EnterRow
	public void insertNewContact()
	{
		phoneBook.add(getContact());
	}

	public boolean insertWithNumber(String firstName, String lastName, String number)
	{
		try
		{
			phoneBook.add(new PhoneBookEntry(firstName, lastName, number));
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public String thatNumberOfIs(String firstName, String lastName)
	{
		return phoneBook.findNumber(firstName, lastName);
	}

	public boolean updateNumberOfTo(String firstName, String lastName, String number)
	{
		try
		{
			phoneBook.updateNumber(firstName, lastName, number);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
