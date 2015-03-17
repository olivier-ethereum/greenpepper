package com.greenpepper.annotation;

import java.util.Locale;

import junit.framework.TestCase;

import com.greenpepper.GreenPepper;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.util.FakeText;

public class WrongAnnotationTest extends TestCase
{
    private FakeText text;
    private WrongAnnotation wrong;

    @Override
    public void tearDown() {
        GreenPepper.setLocale(Locale.getDefault());
    }

    protected void setUp() throws Exception
    {
        GreenPepper.setLocale(Locale.ENGLISH);
        text = new FakeText( "expected" );
        wrong = new WrongAnnotation();
    }

    public void testColorsInRedAndDoNotReplaceTextByDefault()
    {
        wrong.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "expected", text.getContent() );
    }

    public void testCanReplaceTextWithExpectationAndActual()
    {
        wrong.giveDetails( ShouldBe.equal( "expected" ), "actual" );
        wrong.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<b>Expected:</b> expected <b>Received:</b> actual", text.getContent() );
    }
}
