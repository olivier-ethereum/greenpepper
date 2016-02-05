package com.greenpepper.annotation;

import com.greenpepper.Text;
import com.greenpepper.TypeConversion;

/**
 * <p>IgnoredAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class IgnoredAnnotation implements Annotation
{
    private final Object actual;

    /**
     * <p>Constructor for IgnoredAnnotation.</p>
     *
     * @param actual a {@link java.lang.Object} object.
     */
    public IgnoredAnnotation(Object actual)
    {
        this.actual = actual;
    }

    /** {@inheritDoc} */
    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.GRAY );
        text.setContent( TypeConversion.toString( actual ) );
    }
}
