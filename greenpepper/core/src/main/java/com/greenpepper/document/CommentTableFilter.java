package com.greenpepper.document;

import com.greenpepper.Example;
import static com.greenpepper.util.ExampleUtil.contentOf;

/**
 * <p>CommentTableFilter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CommentTableFilter extends AbstractTableFilter
{
    private static final String BEGIN_INFO = "begin info";
    private static final String END_INFO = "end info";

    /**
     * <p>Constructor for CommentTableFilter.</p>
     */
    public CommentTableFilter()
    {
        super( BEGIN_INFO );
    }

    /** {@inheritDoc} */
    protected Example doFilter( Example example )
    {
        for (Example table = example.nextSibling(); table != null; table = table.nextSibling())
        {
            if (END_INFO.equalsIgnoreCase( contentOf( table.at( 0, 0, 0 ) ) )) return table.nextSibling();
        }
        return null;
    }
}
