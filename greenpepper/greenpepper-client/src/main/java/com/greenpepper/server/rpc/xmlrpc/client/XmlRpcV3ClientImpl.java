
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
import static com.greenpepper.server.GreenPepperServerErrorKey.XML_RPC_URL_NOTFOUND;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcLiteHttpTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class XmlRpcV3ClientImpl
		implements XmlRpcClientExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlRpcV3ClientImpl.class);

	private final XmlRpcClient client;

	/**
	 * <p>Constructor for XmlRpcV3ClientImpl.</p>
	 *
	 * @param url a {@link java.lang.String} object.
	 * @throws com.greenpepper.server.rpc.xmlrpc.client.XmlRpcClientExecutorException if any.
	 */
	public XmlRpcV3ClientImpl(String url)
			throws XmlRpcClientExecutorException {

		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(url));

			this.client = new XmlRpcClient();

			client.setTransportFactory(new XmlRpcLiteHttpTransportFactory(client));
			client.setConfig(config);
		}
		catch (MalformedURLException ex) {
			throw new XmlRpcClientExecutorException(XML_RPC_URL_NOTFOUND, ex);
		}
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public Object execute(String method, Vector params)
			throws XmlRpcClientExecutorException {
		try {
            LOGGER.debug("executing method {} , with params {}", method, params);
			Object result = client.execute(method, params);

			Vector<Object> execParams;

			// Patch xmlrpc 3.x+
			if (result.getClass().isArray()) {
				Object[] resultArray = (Object[])result;
				execParams = new Vector<Object>();
				execParams.addAll(Arrays.asList(resultArray));
			}
			else {
				execParams = (Vector<Object>)result;
			}

			return execParams;
		}
		catch (XmlRpcException ex) {
			throw new XmlRpcClientExecutorException(CALL_FAILED, ex);
		}
	}
}
