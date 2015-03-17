package com.greenpepper.server;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GreenPepperServerTest {

    @Test
    public void testVersionDate() throws Exception {
        assertNotNull(GreenPepperServer.versionDate());
        assertNotNull(GreenPepperServer.VERSION);
    }

}
