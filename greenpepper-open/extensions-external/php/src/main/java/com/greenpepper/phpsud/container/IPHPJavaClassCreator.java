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

/**
 * <p>IPHPJavaClassCreator interface.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public interface IPHPJavaClassCreator {

	/**
	 * <p>getGeneratedClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> getGeneratedClass();

	/**
	 * <p>createNewObject.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	Object createNewObject(String id);

}