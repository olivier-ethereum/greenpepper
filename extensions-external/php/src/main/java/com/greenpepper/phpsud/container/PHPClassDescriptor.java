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
import java.util.Set;

import org.apache.log4j.Logger;

import com.greenpepper.phpsud.exceptions.NoConverterFoundException;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.helper.Helper;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * <p>PHPClassDescriptor class.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public class PHPClassDescriptor {
	
	private static final Logger LOGGER = Logger.getLogger(PHPClassDescriptor.class);

	private Hashtable<String, PHPMethodDescriptor> methodList;
	
	private Hashtable<String, String> staticVarList;
	
	/**
	 * <p>Getter for the field <code>staticVarList</code>.</p>
	 *
	 * @return a {@link java.util.Hashtable} object.
	 */
	public Hashtable<String, String> getStaticVarList() {
		return staticVarList;
	}

	private String className;
	
	private IPHPJavaClassCreator classCreator;

	private PHPClassDescriptor(String className, PHPContainer container) throws PHPException {
		this.className = className;
		this.methodList = new Hashtable<String, PHPMethodDescriptor>();
		this.staticVarList = new Hashtable<String, String>();
		String s = container.get(PHPInterpeter.getMethodsInClass(className));
		String [] methods = Helper.parseArray(s);
		for(String m : methods) {
			this.methodList.put(m.toLowerCase(), new PHPMethodDescriptor(this, m, container));		
		}
		String s2 = container.get(PHPInterpeter.getStaticVar(className));
		String [] statics = Helper.parseArray(s2);
		for(String var : statics) {
			this.staticVarList.put(var.toLowerCase(), '$' + var);
		}
		this.classCreator = container.getClassCreatorFactory().getClassCreator(container, this);
	}

	/**
	 * <p>getMethod.</p>
	 *
	 * @param methodName a {@link java.lang.String} object.
	 * @return a {@link com.greenpepper.phpsud.container.PHPMethodDescriptor} object.
	 */
	public PHPMethodDescriptor getMethod(String methodName) {
		return methodList.get(methodName.toLowerCase());
	}
	
	/**
	 * <p>Getter for the field <code>methodList</code>.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<String> getMethodList() {
		return methodList.keySet();
	}

	/**
	 * <p>Getter for the field <code>className</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return getClassName();
	}
	
	/**
	 * <p>getValue.</p>
	 *
	 * @param param a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws com.greenpepper.phpsud.exceptions.PHPException if any.
	 */
	public String getValue(String param) throws PHPException {
		PHPMethodDescriptor m = getMethod("parse"); 
		if (m != null) {
			LOGGER.debug("Use parse method for class " + this);
			return getClassName() + "::parse('" + param + "')";
		}
		String staticVar = staticVarList.get(param.toLowerCase());
		if (staticVar != null) {
			LOGGER.debug("Using static var : " + staticVar);
			return getClassName() + "::" + staticVar;
		}
		LOGGER.error(this + " : no method parse and no static var " + param);
		throw new NoConverterFoundException(this + " : no method parse and no static var " + param);
	}
	
	/**
	 * <p>getGeneratedClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	public Class<?> getGeneratedClass() { 
		return classCreator.getGeneratedClass();
	}
	
	/**
	 * <p>createNewObject.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public Object createNewObject(String id) { 
		return classCreator.createNewObject(id);
	}
	
	/**
	 * <p>getClassDescriptor.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param container a {@link com.greenpepper.phpsud.container.PHPContainer} object.
	 * @param classCache a {@link java.util.Hashtable} object.
	 * @return a {@link com.greenpepper.phpsud.container.PHPClassDescriptor} object.
	 * @throws com.greenpepper.phpsud.exceptions.PHPException if any.
	 */
	public static PHPClassDescriptor getClassDescriptor(String className, PHPContainer container, Hashtable<String, PHPClassDescriptor> classCache) throws PHPException {
		PHPClassDescriptor c = classCache.get(className.toLowerCase());
		if (c != null) {
			return c;
		}
		Object o = container.getObjectParser().parse("class_exists('" + className + "')");
		if (o instanceof Boolean) {
			Boolean exist = (Boolean) o;
			if (exist) {
				c = new PHPClassDescriptor(className, container);
				LOGGER.info("Add in classcache " + className);
				classCache.put(className.toLowerCase(), c);
				return c;
			}
		}
		return null;
	}

}
