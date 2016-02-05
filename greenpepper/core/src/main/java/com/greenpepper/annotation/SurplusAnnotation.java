package com.greenpepper.annotation;

import static com.greenpepper.GreenPepper.$;

import com.greenpepper.Text;

/**
 * <p>SurplusAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SurplusAnnotation implements Annotation
{
    /** {@inheritDoc} */
    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.RED );
        text.setContent( message() + text.getContent() );
    }
    
    private String message()
    {
        StringBuilder message = new StringBuilder();
        message.append( "<em>" ).append( $( "surplus" ) ).append( "</em> " );
        return message.toString();
    }
}
