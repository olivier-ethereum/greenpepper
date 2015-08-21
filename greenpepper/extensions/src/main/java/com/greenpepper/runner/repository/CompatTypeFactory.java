package com.greenpepper.runner.repository;

import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlrpc.common.TypeFactory;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.parser.ObjectArrayParser;
import org.apache.xmlrpc.parser.TypeParser;
import org.apache.xmlrpc.serializer.ObjectArraySerializer;

import com.greenpepper.util.CollectionUtil;

/**
 * TypeFactory to keep compatibility when migrating to XMLRPC3
 * <ul>
 * <li> convert "array" to "Vector"</li>
 * </ul>
 * @author oaouattara
 *
 */
public class CompatTypeFactory extends TypeFactoryImpl {

    
    public static class ObjectArrayToVectorParser extends ObjectArrayParser implements TypeParser {

        public ObjectArrayToVectorParser(XmlRpcStreamConfig pConfig, NamespaceContextImpl pContext, TypeFactory pFactory) {
            super(pConfig, pContext, pFactory);
        }

        @Override
        public void setResult(Object pResult) {
            if (pResult != null && !pResult.getClass().isArray()) {
                throw new IllegalArgumentException("Unsupported type form result. Found : " + pResult.getClass().getName());
            }
            super.setResult(CollectionUtil.toVector((Object[])pResult));
        }

    }

    public CompatTypeFactory(XmlRpcController pController) {
        super(pController);
    }

    public TypeParser getParser(XmlRpcStreamConfig pConfig, NamespaceContextImpl pContext, String pURI, String pLocalName) {
        if (ObjectArraySerializer.ARRAY_TAG.equals(pLocalName)) {
            return new ObjectArrayToVectorParser(pConfig,pContext,this);
        } else {
            return super.getParser(pConfig, pContext, pURI, pLocalName);
        }
    }
    
}
