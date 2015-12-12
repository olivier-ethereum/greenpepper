package com.greenpepper.util;

import com.greenpepper.Example;
import com.greenpepper.annotation.Annotation;
import com.greenpepper.annotation.EnteredAnnotation;
import com.greenpepper.annotation.ExceptionAnnotation;
import com.greenpepper.annotation.RightAnnotation;
import com.greenpepper.annotation.SkippedAnnotation;
import com.greenpepper.annotation.WrongAnnotation;

public interface MarkupPrinter<T extends Example> {

    String print(T cell);

    public static class Default implements MarkupPrinter<Example>
    {
		public String print(Example cell) {
            return cell.getContent();
		}
    }
    
    public static class WithResults implements MarkupPrinter<FakeExample>
    {
        public String print(FakeExample cell) {
            StringBuffer output = new StringBuffer();
            Annotation annotation = cell.getAnnotation();
            if (annotation != null) {
                output.append("{");
                if (annotation instanceof ExceptionAnnotation) {
                    output.append("E:").append(((ExceptionAnnotation) annotation).getExceptionMessage());
                } else if (annotation instanceof RightAnnotation) {
                    output.append("OK");
                } else if (annotation instanceof EnteredAnnotation) {
                    output.append("OK");
                } else if (annotation instanceof WrongAnnotation) {
                    output.append("KO");
                } else if (annotation instanceof SkippedAnnotation) {
                    output.append("SKIP");
                } else {
                    output.append(annotation.getClass().getSimpleName());
                }
                
                output.append("} ");
            }
            output.append(cell.getContent());
            return output.toString();
        }
    }
}
