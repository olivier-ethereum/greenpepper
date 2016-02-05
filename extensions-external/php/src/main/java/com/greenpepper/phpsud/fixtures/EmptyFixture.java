/*
 * Copyright (c) 2008 Pyxis Technologies inc.
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
 *
 * IMPORTANT NOTE :
 * Kindly contributed by Bertrand Paquet from Octo Technology (http://www.octo.com)
 */
package com.greenpepper.phpsud.fixtures;

import org.apache.log4j.Logger;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;

/**
 * <p>EmptyFixture class.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public class EmptyFixture implements Fixture {

	private static final Logger LOGGER = Logger.getLogger(EmptyFixture.class);

	private Object o;
	
	private Fixture fixture;
	
	/**
	 * <p>Constructor for EmptyFixture.</p>
	 *
	 * @param fixture a {@link com.greenpepper.reflect.Fixture} object.
	 * @param o a {@link java.lang.Object} object.
	 */
	public EmptyFixture(Fixture fixture, Object o) {
		this.fixture = fixture;
		this.o = o;
	}
	
	/** {@inheritDoc} */
	public boolean canCheck(String arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	/** {@inheritDoc} */
	public boolean canSend(String arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	/** {@inheritDoc} */
	public Message check(String arg0) throws NoSuchMessageException {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	/** {@inheritDoc} */
	public Fixture fixtureFor(Object arg0) {
		LOGGER.debug("Calling parent fixture for");
		return fixture.fixtureFor(arg0);
	}

	/**
	 * <p>getTarget.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getTarget() {
		LOGGER.debug("Returning object");
		return o;
	}

	/** {@inheritDoc} */
	public Message send(String arg0) throws NoSuchMessageException {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

}
