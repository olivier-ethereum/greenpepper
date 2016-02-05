package com.greenpepper.annotation;

import static com.greenpepper.GreenPepper.$;

import com.greenpepper.Text;

/**
 * <p>NotEnteredAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class NotEnteredAnnotation implements Annotation
{
    /** {@inheritDoc} */
    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.RED );
        text.setContent( message() );
    }
    
    private String message()
    {
        StringBuilder message = new StringBuilder();
        message.append( "<em>" ).append( $( "not_entered" ) ).append( "</em>" );
        return message.toString();
    }
}
