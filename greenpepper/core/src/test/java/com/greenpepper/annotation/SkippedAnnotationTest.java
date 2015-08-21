package com.greenpepper.annotation;

import java.util.Locale;

import junit.framework.TestCase;

import com.greenpepper.GreenPepper;
import com.greenpepper.util.FakeText;

public class SkippedAnnotationTest
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

    public void testColorsInOrangeAndDisplaysActualValueAsSkipped()
    {
        FakeText text = new FakeText( "text" );

        SkippedAnnotation annotation = new SkippedAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.ORANGE, text.getStyle( "background-color" ) );
        assertEquals( "<em>Skipped</em>", text.getContent() );
    }
}