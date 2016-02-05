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
package com.greenpepper.phpsud.container;

import java.util.Hashtable;

import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.exceptions.PHPException;

/**
 * <p>PHPClassPath class.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public class PHPClassPath {

	private Hashtable<String, PHPClassDescriptor> classCache;
	
	/**
	 * <p>Constructor for PHPClassPath.</p>
	 */
	public PHPClassPath() {
		classCache = new Hashtable<String, PHPClassDescriptor>();
	}
	
	/**
	 * <p>getClassDescriptor.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param container a {@link com.greenpepper.phpsud.container.PHPContainer} object.
	 * @return a {@link com.greenpepper.phpsud.container.PHPClassDescriptor} object.
	 * @throws com.greenpepper.phpsud.exceptions.PHPException if any.
	 */
	public PHPClassDescriptor getClassDescriptor(String className, PHPContainer container) throws PHPException {
		return PHPClassDescriptor.getClassDescriptor(className, container, classCache);
	}



}
