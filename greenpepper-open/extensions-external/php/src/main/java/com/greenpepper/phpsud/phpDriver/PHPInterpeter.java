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
package com.greenpepper.phpsud.phpDriver;

/**
 * <p>PHPInterpeter class.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public class PHPInterpeter {

	/**
	 * <p>getObject.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getObject(String id) {
		return "phpsud_getObject(\"" + id + "\")";
	}
	
	/**
	 * <p>saveObject.</p>
	 *
	 * @param obj a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String saveObject(String obj) {
		return "phpsud_saveObject(" + obj + ")";
	}
	
	/**
	 * <p>getStaticVar.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getStaticVar(String className) {
		return serialize("phpsud_getStaticVar(\"" + className + "\")");
	}
	
	/**
	 * <p>getMethodsInClass.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getMethodsInClass(String className) {
		return serialize("phpsud_getMethodsInClass(\"" + className + "\")");
	}
	
	/**
	 * <p>getParamsInMethods.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param methodName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getParamsInMethods(String className, String methodName) {
		return serialize("phpsud_getParamsInMethod(\"" + className + "\", \"" + methodName + "\")");
	}
	
	/**
	 * <p>getArray.</p>
	 *
	 * @param array a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getArray(String array) {
		return "phpsud_getArray(" + array + ")";
	}
	
	/**
	 * <p>getClass.</p>
	 *
	 * @param obj a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getClass(String obj) {
		return "get_class(" + obj + ")";
	}
	
	/**
	 * <p>getType.</p>
	 *
	 * @param obj a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getType(String obj) {
		return "gettype(" + obj + ")";
	}
	
	/**
	 * <p>serialize.</p>
	 *
	 * @param obj a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String serialize(String obj) {
		return "serialize(" + obj + ")";
	}
	
	private static int index = 0;
	
	/**
	 * <p>getVar.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getVar() {
		String varName = "$phpsud_" + index;
		index ++;
		return varName;
	}
}
