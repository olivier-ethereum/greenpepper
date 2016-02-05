package com.greenpepper.annotation;

import java.util.UUID;

import com.greenpepper.Text;
import com.greenpepper.util.ExceptionUtils;

/**
 * <p>ExceptionAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ExceptionAnnotation implements Annotation {

    private final Throwable error;

    /**
     * <p>Constructor for ExceptionAnnotation.</p>
     *
     * @param error a {@link java.lang.Throwable} object.
     */
    public ExceptionAnnotation(Throwable error) {
        this.error = error;
    }

    /** {@inheritDoc} */
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.YELLOW);
        StringBuilder textContent = new StringBuilder(text.getContent());
        textContent.append("<hr/><div class=\"greenpepper-report-exception\"><font>")
            .append(error.getClass().getSimpleName())
            .append(": ")
            .append(error.getMessage())
            .append("</font>");
        
        UUID randomUUID = UUID.randomUUID();
        String expandButtonID = randomUUID.toString() + "-show";
        textContent.append("\n<a href=\"#").append(expandButtonID).append("\" ")
        .append("class=\"greenpepper-report-stacktrace-show\" ")
        .append("id=\"").append(expandButtonID).append("\" >+</a>");
        
        
        String collapseButtonID = randomUUID.toString() + "-hide";
        textContent.append("\n<a href=\"#").append(collapseButtonID).append("\" ")
        .append("class=\"greenpepper-report-stacktrace-hide\" ")
        .append("id=\"").append(collapseButtonID).append("\" >-</a>");
        
        
        textContent.append("\n<pre class=\"greenpepper-report-stacktrace\">")
            .append(ExceptionUtils.stackTrace(error, "\n", 10))
            .append("</pre></div>");
        text.setContent(textContent.toString());
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return ExceptionUtils.stackTrace(error, "\n", 10);
    }
    
    /**
     * <p>getExceptionMessage.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getExceptionMessage() {
        return error.getMessage();
    }
}
