package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.Example;

/**
 * <p>Annotate class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class Annotate
{
    /**
     * <p>right.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub right(final Annotatable annotatable)
    {
        return new AnnotateRight( annotatable );
    }

    /**
     * <p>wrong.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub wrong(Annotatable annotatable)
    {
        return new AnnotateWrong( annotatable, false );
    }

    /**
     * <p>wrongWithDetails.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub wrongWithDetails(Annotatable annotatable)
    {
        return new AnnotateWrong( annotatable, true );
    }

    /**
     * <p>exception.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub exception(Annotatable annotatable)
    {
        return new AnnotateException( annotatable );
    }

    private Annotate()
    {
    }

    /**
     * <p>withDetails.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub withDetails(Annotatable annotatable)
    {
        return new AnnotateExample( annotatable, true );
    }

    /**
     * <p>withoutDetail.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub withoutDetail(Annotatable annotatable)
    {
        return new AnnotateExample( annotatable, false );
    }

    /**
     * <p>entered.</p>
     *
     * @param annotatable a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub entered(Example annotatable)
    {
        return new AnnotateEntered( annotatable );
    }
    
    /**
     * <p>notEntered.</p>
     *
     * @param annotatable a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub notEntered(Example annotatable)
    {
        return new AnnotateNotEntered( annotatable );
    }

    /**
     * <p>ignored.</p>
     *
     * @param annotatable a {@link com.greenpepper.Annotatable} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub ignored(Annotatable annotatable)
    {
        return new AnnotateIgnored( annotatable );
    }
    
    /**
     * <p>setup.</p>
     *
     * @param annotatable a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.call.Stub} object.
     */
    public static Stub setup(Example annotatable)
    {
        return new AnnotateSetup( annotatable );
    }
}
