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

import com.greenpepper.phpsud.exceptions.PHPException;

/**
 * <p>IObjectParser interface.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public interface IObjectParser {
	
	/**
	 * <p>parse.</p>
	 *
	 * @param expr a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws com.greenpepper.phpsud.exceptions.PHPException if any.
	 */
	Object parse(String expr) throws PHPException;
	
	/**
	 * <p>setPHPContainer.</p>
	 *
	 * @param container a {@link com.greenpepper.phpsud.container.PHPContainer} object.
	 */
	void setPHPContainer(PHPContainer container);

}
