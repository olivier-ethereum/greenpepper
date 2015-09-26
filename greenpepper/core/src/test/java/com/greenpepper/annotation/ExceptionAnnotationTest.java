package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;

import junit.framework.TestCase;

public class ExceptionAnnotationTest extends TestCase
{
    private FakeText text;

    protected void setUp() throws Exception
    {
        text = new FakeText( "content" );
    }

    public void testColorsInYellowAndAddErrorBacktraceToText()
    {
        Throwable error = new RuntimeException( "error message" );
        error.setStackTrace( new StackTraceElement[]{new StackTraceElement( "Class", "method", "Class.java", -1 )} );

        ExceptionAnnotation exception = new ExceptionAnnotation( error );
        exception.writeDown( text );

        assertEquals( "content<hr/><pre class=\"greenpepper-report-exception\"><font>" + error.getMessage() + "</font><div class=\"greenpepper-report-stacktrace\">java.lang.RuntimeException: error message\nClass.method(Class.java)</div></pre>", text.getContent() );
    }
    

    public void testColorsInYellowAndAddErrorBacktrace3linesToText()
    {
        Throwable error = new RuntimeException( "error message" );
        error.setStackTrace( new StackTraceElement[]{ new StackTraceElement( "Class", "method", "Class.java", -1 ),
                new StackTraceElement( "Class", "methodA", "Class.java", -2 ),
                new StackTraceElement( "Class", "methodB", "Class.java", -3 )} );

        ExceptionAnnotation exception = new ExceptionAnnotation( error );
        exception.writeDown( text );

        assertEquals( "content<hr/><pre class=\"greenpepper-report-exception\"><font>" + error.getMessage() + "</font><div class=\"greenpepper-report-stacktrace\">java.lang.RuntimeException: error message\n"
                + "Class.method(Class.java)\n"
                + "Class.methodA(Native Method)\n"
                + "Class.methodB(Class.java)</div></pre>", text.getContent() );
    }
}
