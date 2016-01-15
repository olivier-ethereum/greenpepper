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
package com.greenpepper.maven.runner.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import com.greenpepper.GreenPepper;

public class ReflectionUtilsTest {

    @Test
    public void canGetValueOfADeclaredField() throws Exception {
        String valueA = (String) ReflectionUtils.getDeclaredFieldValue(new ObjectA(), "myField");
        assertEquals("A", valueA);
    }

    @Test
    public void forAnUnknowFieldShouldThrowException() {
        try {
            ReflectionUtils.getDeclaredFieldValue(new ObjectA(), "unknowField");

            fail();
        } catch (Exception ex) {
            // Expected
        }
    }

    @Test
    public void invokeMainMethodShouldChangeFieldValueProperly() throws Exception {

        ReflectionUtils.invokeMain(ObjectA.class, Arrays.asList("C"));
        assertEquals("C", ObjectA.myMainField);
    }

    @Test
    public void invokeLaunchMethodShouldChangeFieldValueProperly() throws Exception {

        int exitcode = ReflectionUtils.invokeLaunch(ObjectA.class, Arrays.asList("D"));
        assertEquals(0, exitcode);
        assertEquals("D", ObjectA.myLaunchField);

    }

    @Test(expected = NoSuchMethodException.class)
    public void invokeLaunchMethodShouldThrowAnException() throws Exception {

        ReflectionUtils.invokeLaunch(ObjectB.class, Arrays.asList("D"));
    }

    @Test
    public void activatingTheDebugMode() throws Exception {
        GreenPepper.setDebugEnabled(false);
        ReflectionUtils.setDebugEnabled(Thread.currentThread().getContextClassLoader(), true);
        assertTrue(GreenPepper.isDebugEnabled());
    }

    public static class ObjectA {

        @SuppressWarnings("unused")
        private String myField = "A";
        private static String myMainField = "B";
        private static String myLaunchField = "Z";

        public static void main(String... args) throws Exception {
            myMainField = args[0];
        }

        public static int launch(String... args) throws Exception {
            myLaunchField = args[0];
            return 0;
        }
    }

    public static class ObjectB {

        public static String launch(String... args) throws Exception {
            return "OK";
        }
    }
}
