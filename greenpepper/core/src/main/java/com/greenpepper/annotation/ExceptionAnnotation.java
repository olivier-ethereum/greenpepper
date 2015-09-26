package com.greenpepper.annotation;

import com.greenpepper.Text;
import com.greenpepper.util.ExceptionUtils;

public class ExceptionAnnotation implements Annotation {

    private final Throwable error;

    public ExceptionAnnotation(Throwable error) {
        this.error = error;
    }

    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.YELLOW);
        StringBuilder textContent = new StringBuilder(text.getContent());
        textContent.append("<hr/><pre class=\"greenpepper-report-exception\"><font>").append(error.getMessage()).append("</font>");
        textContent.append("<div class=\"greenpepper-report-stacktrace\">").append(ExceptionUtils.stackTrace(error, "\n", 10)).append("</div></pre>");
        text.setContent(textContent.toString());
    }

    public String toString() {
        return ExceptionUtils.stackTrace(error, "\n", 10);
    }
}
