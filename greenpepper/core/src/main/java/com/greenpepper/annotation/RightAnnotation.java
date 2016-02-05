package com.greenpepper.annotation;

import com.greenpepper.Text;

/**
 * <p>RightAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class RightAnnotation implements Annotation
{
    /** {@inheritDoc} */
    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.GREEN );
    }
}
