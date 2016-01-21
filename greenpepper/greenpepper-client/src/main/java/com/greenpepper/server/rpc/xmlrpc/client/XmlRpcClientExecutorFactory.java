/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.server.rpc.xmlrpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.server.GreenPepperServerErrorKey;

public class XmlRpcClientExecutorFactory {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlRpcClientExecutorFactory.class);


    public static XmlRpcClientExecutor newExecutor(String url) throws XmlRpcClientExecutorException {

        try {
            LOGGER.debug("Instanciating new executor for url {} ", url);
            return new XmlRpcV2ClientImpl(url);
            // if (loadClass("org.apache.xmlrpc.client.XmlRpcClient") == null) {
            // return new XmlRpcV2ClientImpl(url);
            // } else {
            // return new XmlRpcV3ClientImpl(url);
            // }

        } catch (Exception ex) {
            throw new XmlRpcClientExecutorException(GreenPepperServerErrorKey.GENERAL_ERROR, ex);
        }
    }

}
