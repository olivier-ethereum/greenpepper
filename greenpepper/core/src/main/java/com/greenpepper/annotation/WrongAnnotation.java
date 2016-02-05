package com.greenpepper.annotation;

import static com.greenpepper.GreenPepper.$;
import com.greenpepper.Text;
import com.greenpepper.TypeConversion;
import com.greenpepper.expectation.Expectation;

/**
 * <p>WrongAnnotation class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class WrongAnnotation implements Annotation
{
    private Expectation expected;
    private Object actual;
    private boolean detailed;

    /** {@inheritDoc} */
    public void writeDown( Text text )
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.RED );
        if (detailed) text.setContent( message() );
    }

    private String message()
    {
        StringBuilder message = new StringBuilder();
        message.append( "<b>" ).append( $( "expected" ) ).append( ":</b> " );
        expected.describeTo( message );
        message.append( " <b>" ).append( $( "received" ) ).append( ":</b> " ).append( TypeConversion.toString( actual ) );
        return message.toString();
    }

    /**
     * <p>giveDetails.</p>
     *
     * @param expected a {@link com.greenpepper.expectation.Expectation} object.
     * @param actual a {@link java.lang.Object} object.
     */
    public void giveDetails( Expectation expected, Object actual )
    {
        this.expected = expected;
        this.actual = actual;
        this.detailed = true;
    }

    /**
     * <p>isDetailed.</p>
     *
     * @return a boolean.
     */
    public boolean isDetailed()
    {
        return detailed;
    }
}
