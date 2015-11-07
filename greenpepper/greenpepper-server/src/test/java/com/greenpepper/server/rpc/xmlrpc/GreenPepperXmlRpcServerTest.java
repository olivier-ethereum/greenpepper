package com.greenpepper.server.rpc.xmlrpc;

import static com.greenpepper.server.GreenPepperServerErrorKey.SUCCESS;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.server.GreenPepperServerService;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;

public class GreenPepperXmlRpcServerTest {

    private GreenPepperServerService service = createNiceMock(GreenPepperServerService.class);
    private GreenPepperXmlRpcServer greenPepperXmlRpcServer;

    @Before
    public void createGreenPepperXmlRpcServer() throws Exception {
        greenPepperXmlRpcServer = new GreenPepperXmlRpcServer(service);
    }

    @Test
    public void shouldRemoveSpecificationSystemUnderTestWithMinimalInformation() throws Exception {
        @SuppressWarnings("serial")
        Vector<Object> specificationParams = new Vector<Object>() {{ add("specName"); add(new Vector<Object>() {{ add("repo.name"); add("repo.uid");}});}};
        @SuppressWarnings("serial")
        Vector<Object> systemUnderTestParams = new Vector<Object>() {{ add("sutName"); add(new Vector<Object>() {{ add("projectname");}});}};

        Capture<SystemUnderTest> systemUnderTest = newCapture();
        Capture<Specification> specification = newCapture();
        service.removeSpecificationSystemUnderTest(capture(systemUnderTest), capture(specification));
        expectLastCall();
        replay(service);

        String output = greenPepperXmlRpcServer.removeSpecificationSystemUnderTest(systemUnderTestParams, specificationParams);

        verify(service);
        assertEquals(SUCCESS, output);
        assertEquals("sutName", systemUnderTest.getValue().getName());
        assertEquals("projectname", systemUnderTest.getValue().getProject().getName());
        assertEquals("repo.uid", specification.getValue().getRepository().getUid());
        assertEquals("specName", specification.getValue().getName());
    }

}
