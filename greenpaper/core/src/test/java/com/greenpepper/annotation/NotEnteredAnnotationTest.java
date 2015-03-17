package com.greenpepper.annotation;

import java.util.Locale;

import junit.framework.TestCase;

import com.greenpepper.GreenPepper;
import com.greenpepper.util.FakeText;

public class NotEnteredAnnotationTest extends TestCase
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

        NotEnteredAnnotation annotation = new NotEnteredAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<em>Not entered</em>", text.getContent() );
    }
}
