package com.greenpepper.annotation;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

        String content = text.getContent();
        Document doc = Jsoup.parseBodyFragment(content);
        Elements links = doc.select("a");
        assertNotNull(links);
        assertEquals(2, links.size());
        Elements hideLink = doc.select("a.greenpepper-report-stacktrace-hide");
        assertNotNull(hideLink);
        assertEquals(1, hideLink.size());
        assertEquals("#"+hideLink.get(0).id(),hideLink.get(0).attr("href")); 
        Elements showLink = doc.select("a.greenpepper-report-stacktrace-show");
        assertNotNull(showLink);
        assertEquals(1, showLink.size());
        assertEquals("#"+showLink.get(0).id(),showLink.get(0).attr("href")); 
        assertFalse(StringUtils.equals(hideLink.get(0).id(), showLink.get(0).id()));
        
        Elements reportdiv = doc.select("div.greenpepper-report-exception");
        Elements reportdivChildren = reportdiv.get(0).children();
        assertEquals(4, reportdivChildren.size());
        assertEquals("RuntimeException: error message" , reportdivChildren.get(0).text());

        assertEquals("greenpepper-report-stacktrace", reportdivChildren.get(3).className());
        assertEquals("pre", reportdivChildren.get(3).tagName());
        assertEquals("java.lang.RuntimeException: error message\nClass.method(Class.java)" , reportdivChildren.get(3).text());
                
    }
    

    public void testColorsInYellowAndAddErrorBacktrace3linesToText()
    {
        Throwable error = new RuntimeException( "error message" );
        error.setStackTrace( new StackTraceElement[]{ new StackTraceElement( "Class", "method", "Class.java", -1 ),
                new StackTraceElement( "Class", "methodA", "Class.java", -2 ),
                new StackTraceElement( "Class", "methodB", "Class.java", -3 )} );

        ExceptionAnnotation exception = new ExceptionAnnotation( error );
        exception.writeDown( text );

        String content = text.getContent();
        Document doc = Jsoup.parseBodyFragment(content);
        Elements reportdiv = doc.select("div.greenpepper-report-exception");
        Elements reportdivChildren = reportdiv.get(0).children();
        assertEquals("java.lang.RuntimeException: error message\n"
                + "Class.method(Class.java)\n"
                + "Class.methodA(Native Method)\n"
                + "Class.methodB(Class.java)" , reportdivChildren.get(3).text());
    }
}
