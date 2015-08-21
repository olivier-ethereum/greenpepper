package com.greenpepper.runner.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.TypeFactory;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.junit.Test;

import com.greenpepper.runner.repository.CompatTypeFactory.ObjectArrayToVectorParser;

public class ObjectArrayToVectorParserTest {

    @Test
    public void shouldSetTheResultToVector() throws XmlRpcException {
        ObjectArrayToVectorParser parser = new ObjectArrayToVectorParser((XmlRpcStreamConfig) null, (NamespaceContextImpl) null, (TypeFactory) null);
        parser.setResult(new String[]{});
        assertTrue(parser.getResult() instanceof Vector<?>);
        parser.setResult(new String[]{"toto", "tata"});
        assertTrue(parser.getResult() instanceof Vector<?>);
        Vector<?> vector = (Vector<?>)parser.getResult();
        assertEquals(2, vector.size());
        parser.setResult(new String[][]{new String[]{"toto"}, new String[]{"tata"}});
        assertTrue(parser.getResult() instanceof Vector<?>);
        vector = (Vector<?>)parser.getResult();
        assertEquals(2, vector.size());
    }


}
