
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.server.rpc.xmlrpc.client;

import java.util.Vector;
public interface XmlRpcClientExecutor {

	/**
	 * <p>execute.</p>
	 *
	 * @param method a {@link java.lang.String} object.
	 * @param params a {@link java.util.Vector} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws com.greenpepper.server.rpc.xmlrpc.client.XmlRpcClientExecutorException if any.
	 */
	Object execute(String method, Vector params)
			throws XmlRpcClientExecutorException;
}
