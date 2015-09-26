package com.greenpepper.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.greenpepper.Text;

public class FakeText implements Text
{
    private String text;
    private Map<String, String> styles = new HashMap<String, String>();
    private Set<String> cssClasses = new TreeSet<String>();

    public FakeText(String text)
    {
        this.text = text;
    }

    public void setStyle(String property, String value)
    {
        styles.put( property, value );
    }

    public String getStyle(String property)
    {
        return styles.get( property );
    }

    public void setContent(String content)
    {
        text = content;
    }

    public String getContent()
    {
        return text;
    }

    @Override
    public void setCssClasses(String... classes) {
        Collections.addAll(cssClasses, classes);
    }

    @Override
    public String[] getCssClasses() {
        return cssClasses.toArray(new String[cssClasses.size()]);
    }
}
