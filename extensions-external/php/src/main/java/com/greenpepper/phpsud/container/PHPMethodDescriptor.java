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

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.exceptions.UnknownClassException;
import com.greenpepper.phpsud.exceptions.WrongArgumentCountException;
import com.greenpepper.phpsud.helper.Helper;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * <p>PHPMethodDescriptor class.</p>
 *
 * @author Bertrand Paquet
 * @version $Id: $Id
 */
public class PHPMethodDescriptor {

	private List<String> paramsList;

	private PHPClassDescriptor desc;
	
	private String methodName;
	
	private PHPContainer container;

	/**
	 * <p>Constructor for PHPMethodDescriptor.</p>
	 *
	 * @param desc a {@link com.greenpepper.phpsud.container.PHPClassDescriptor} object.
	 * @param methodName a {@link java.lang.String} object.
	 * @param container a {@link com.greenpepper.phpsud.container.PHPContainer} object.
	 * @throws com.greenpepper.phpsud.exceptions.PHPException if any.
	 */
	public PHPMethodDescriptor(PHPClassDescriptor desc, String methodName, PHPContainer container) throws PHPException {
		this.desc = desc;
		this.methodName = methodName;
		this.container = container;
		this.paramsList = new ArrayList<String>();
		String s = container.get(PHPInterpeter.getParamsInMethods(desc.getClassName(), methodName));
		String [] params = Helper.parseArray(s);
		for(String p : params) {
			if ("null".equals(p)) {
				this.paramsList.add(null);
			}
			else {
				this.paramsList.add(p);
			}			
		}
	}
	
	/**
	 * <p>getParamList.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getParamList() {
		return paramsList;
	}

	/**
	 * <p>Getter for the field <code>methodName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMethodName() {
		return methodName;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return getMethodName();
	}
	
	private String getCmd(String id, String ... params) throws PHPException {
		if (params.length != paramsList.size()) {
			throw new WrongArgumentCountException(getMethodName() + " called with " + params.length + " args instead of " + paramsList.size());
		}
		String [] l = new String[params.length];
		for(int i = 0; i < paramsList.size(); i ++) {
			String type = paramsList.get(i);
			if (type == null) {
				l[i] = "'" + params[i] + "'";
			}
			else {
				PHPClassDescriptor c = container.getClassDescriptor(type);
				if (c == null) {
					throw new UnknownClassException(type);
				}
				l[i] = c.getValue(params[i]);
			}
		}
		String cmd = id + "->" + getMethodName() + "(" + Helper.formatParamList(l) + ")";
		return cmd;
	}
	
	/**
	 * <p>getClassDescriptor.</p>
	 *
	 * @return a {@link com.greenpepper.phpsud.container.PHPClassDescriptor} object.
	 */
	public PHPClassDescriptor getClassDescriptor() {
		return desc;
	}

	/**
	 * <p>exec.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @param params a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws com.greenpepper.phpsud.exceptions.PHPException if any.
	 */
	public Object exec(String id, String ... params) throws PHPException {
		return container.getObjectParser().parse(getCmd(id, params));
	}
	
	/**
	 * <p>execNoResponse.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @param params a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	public void execNoResponse(String id, String ... params) throws Exception {
		container.run(getCmd(id, params));
	}
	
}
