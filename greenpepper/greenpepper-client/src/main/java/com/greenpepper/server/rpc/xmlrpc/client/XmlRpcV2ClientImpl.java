
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

import static com.greenpepper.server.GreenPepperServerErrorKey.CALL_FAILED;
import static com.greenpepper.server.GreenPepperServerErrorKey.CONFIGURATION_ERROR;
import static com.greenpepper.server.GreenPepperServerErrorKey.XML_RPC_URL_NOTFOUND;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.secure.SecureXmlRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class XmlRpcV2ClientImpl
		implements XmlRpcClientExecutor {


    private static final Logger LOGGER = LoggerFactory.getLogger(XmlRpcV2ClientImpl.class);

	private final XmlRpcClient client;

	/**
	 * <p>Constructor for XmlRpcV2ClientImpl.</p>
	 *
	 * @param url a {@link java.lang.String} object.
	 * @throws com.greenpepper.server.rpc.xmlrpc.client.XmlRpcClientExecutorException if any.
	 */
	public XmlRpcV2ClientImpl(String url)
			throws XmlRpcClientExecutorException {
		try {
			if (url.toLowerCase().startsWith("https")) {
				this.client = new SecureXmlRpcClient(url);
			}
			else {
				this.client = new XmlRpcClient(url);
			}
		}
		catch (MalformedURLException ex) {
			throw new XmlRpcClientExecutorException(XML_RPC_URL_NOTFOUND, ex);
		}
	}

	/** {@inheritDoc} */
	public Object execute(String method, Vector params)
			throws XmlRpcClientExecutorException {
        LOGGER.debug("executing method {} , with params {}", method, params);

		try {
			return client.execute(method, params);
		}
		catch (XmlRpcException ex) {
			throw new XmlRpcClientExecutorException(CALL_FAILED, ex);
		}
		catch (IOException ex) {
			throw new XmlRpcClientExecutorException(CONFIGURATION_ERROR, ex);
		}
	}
}
