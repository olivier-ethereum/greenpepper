package com.greenpepper.annotation;

import java.util.Locale;

import junit.framework.TestCase;

import com.greenpepper.GreenPepper;
import com.greenpepper.util.FakeText;

public class EnteredAnnotationTest extends TestCase
{

    @Override
    public void setUp() {
        GreenPepper.setLocale(Locale.ENGLISH);
    }

    @Override
    public void tearDown() {
        GreenPepper.setLocale(Locale.getDefault());
    }

    public void testColorsInRedAndDisplaysActualValueAsMissing()
    {
        FakeText text = new FakeText( "text" );

        EnteredAnnotation annotation = new EnteredAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.GREEN, text.getStyle( "background-color" ) );
        assertEquals( "<em>Entered</em>", text.getContent() );
    }
}
