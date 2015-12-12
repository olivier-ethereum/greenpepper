/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  com.greenpepper.server.ServerPropertiesManager
 *  com.greenpepper.server.rpc.xmlrpc.GreenPepperXmlRpcClient
 */
package com.greenpepper.eclipse.rpc.xmlrpc;

import com.greenpepper.eclipse.rpc.xmlrpc.ServerPropertiesManagerImpl;
import com.greenpepper.server.ServerPropertiesManager;
import com.greenpepper.server.rpc.xmlrpc.GreenPepperXmlRpcClient;

public class EclipseXmlRpcGreenpepperClient
extends GreenPepperXmlRpcClient {
    public EclipseXmlRpcGreenpepperClient() {
        super((ServerPropertiesManager)new ServerPropertiesManagerImpl());
    }
}

