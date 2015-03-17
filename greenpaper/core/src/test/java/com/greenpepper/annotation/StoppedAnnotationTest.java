package com.greenpepper.annotation;

import java.util.Locale;

import junit.framework.TestCase;

import com.greenpepper.GreenPepper;
import com.greenpepper.util.FakeText;

public class StoppedAnnotationTest
		extends TestCase
{

    @Override
    public void setUp() {
        GreenPepper.setLocale(Locale.ENGLISH);
    }

    @Override
    public void tearDown() {
        GreenPepper.setLocale(Locale.getDefault());
    }

    public void testColorsInRedAndDisplaysActualValueAsStopped()
    {
        FakeText text = new FakeText( "text" );

        StoppedAnnotation annotation = new StoppedAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<em>Stopped</em>", text.getContent() );
    }
}