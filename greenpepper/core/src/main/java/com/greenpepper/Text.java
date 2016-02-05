package com.greenpepper;

/*
would be nice to consider using a builder style, e.g.
 emphasize();
 emphasizeEnd();
 strong();
 strongEnd();
 ...
 horizontalLine();
 newLine();
 ...
 */
/**
 * <p>Text interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Text
{
    /**
     * <p>setCssClasses.</p>
     *
     * @param classes a {@link java.lang.String} object.
     */
    void setCssClasses(String ... classes);
    /**
     * <p>getCssClasses.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    String[] getCssClasses();
    
    /**
     * <p>setStyle.</p>
     *
     * @param property a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    void setStyle(String property, String value);
    /**
     * <p>getStyle.</p>
     *
     * @param property a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getStyle(String property);

    /**
     * <p>setContent.</p>
     *
     * @param content a {@link java.lang.String} object.
     */
    void setContent(String content);

    /**
     * <p>getContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getContent();
}
