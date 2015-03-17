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
 */
package com.greenpepper.confluence.macros;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import com.atlassian.confluence.renderer.v2.macros.BaseHttpRetrievalMacro;
import com.atlassian.confluence.util.http.HttpResponse;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcMethodName;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.StringUtil;

public abstract class AbstractHttpRetrievalMacro extends BaseHttpRetrievalMacro
{
	private final Logger log = Logger.getLogger(AbstractHttpRetrievalMacro.class);

	public AbstractHttpRetrievalMacro()
	{
		super();
	}

	public String successfulResponse(Map parameters, RenderContext context, String url, HttpResponse response) throws MacroException
	{
		InputStream is = null;

		try
		{
			is = response.getResponse();
			return IOUtils.toString(is);
		}
		catch (IOException ex)
		{
			throw new MacroException(ex);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}

	protected String getParameter(Map parameters, String name)
	{
		String parameter = (String)parameters.get(name);
		return StringUtil.isBlank(parameter) ? null : parameter.trim();
	}

	@SuppressWarnings("unchecked")
	protected Repository getRepository(String url, String handler, String jiraUid) throws GreenPepperServerException
	{
		Vector<Object> response;

		try
		{
			Repository repository = Repository.newInstance(jiraUid);
			Vector params = CollectionUtil.toVector(repository.marshallize());
			XmlRpcClient xmlrpc = new XmlRpcClient(url + "/rpc/xmlrpc");
			String cmdLine = new StringBuffer(handler).append(".").append(XmlRpcMethodName.getRegisteredRepository).toString();
			response = (Vector<Object>)xmlrpc.execute(cmdLine, params);
		}
		catch (XmlRpcException e)
		{
			log.error(e.getMessage(), e);
			throw new GreenPepperServerException(GreenPepperServerErrorKey.CALL_FAILED, e.getMessage());
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
			throw new GreenPepperServerException(GreenPepperServerErrorKey.CONFIGURATION_ERROR, e.getMessage());
		}

		XmlRpcDataMarshaller.checkForErrors(response);

		return XmlRpcDataMarshaller.toRepository(response);
	}
}
