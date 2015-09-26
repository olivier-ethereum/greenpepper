package com.greenpepper.html;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class HtmlExampleTest {

    @Test
    public void printEmptyTag() {
        HtmlExample example = new HtmlExample("", "<pre>", null, "", "</pre>", "", null, null, null);
        testPrint(example, "<pre></pre>");
    }
    
    @Test
    public void printNonEmptyTag() {
        HtmlExample example = new HtmlExample("", "<pre>", null, "content", "</pre>", "", null, null, null);
        String expected = "<pre>content</pre>";
        testPrint(example, expected);
    }
    
    @Test
    public void printNonEmptyTagWithStyle() {
        HtmlExample example = new HtmlExample("", "<pre>", null, "content", "</pre>", "", null, null, null);
        example.setStyle("style1", "whatever");
        testPrint(example, "<pre style=\"style1: whatever; \" >content</pre>");
        example.setStyle("style2", "any");
        testPrint(example, "<pre style=\"style1: whatever; style2: any; \" >content</pre>");
    }
    
    @Test
    public void printNonEmptyTagWithCssClasses() {
        HtmlExample example = new HtmlExample("", "<pre>", null, "content", "</pre>", "", null, null, null);
        example.setCssClasses("class1", "class2");
        testPrint(example, "<pre class=\"class1 class2\" >content</pre>");
    }
    
    

    private void testPrint(HtmlExample example, String expected) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(baos);
        
        // Run
        example.print(out);
        
        // Verify
        out.flush();
        out.close();
        assertEquals(expected, baos.toString());
    }
    
}
