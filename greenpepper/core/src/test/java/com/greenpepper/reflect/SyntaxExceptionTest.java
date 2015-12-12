package com.greenpepper.reflect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SyntaxExceptionTest {

    private SyntaxException syntaxException;

    @Test
    public void testGetMessage() throws Exception {
        syntaxException =  new SyntaxException("TAG_1", "Too many arguments");
        assertEquals("Wrong Syntax for [TAG_1]: Too many arguments", syntaxException.getMessage());
    }

}
