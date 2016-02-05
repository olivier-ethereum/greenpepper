package com.greenpepper.annotation;

import static com.greenpepper.GreenPepper.$;

import com.greenpepper.Text;

/**
 * <p>EnteredAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class EnteredAnnotation implements Annotation
{
    /** {@inheritDoc} */
    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.GREEN );
        text.setContent( message() );
    }
    
    private String message()
    {
        StringBuilder message = new StringBuilder();
        message.append( "<em>" ).append( $( "entered" ) ).append( "</em>" );
        return message.toString();
    }
}
