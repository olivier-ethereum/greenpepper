package com.greenpepper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GreenPepperCoreTest {

    @Test
    public void createGreenPepperCore() {

        assertNotNull(GreenPepperCore.VERSION);
    }

}
