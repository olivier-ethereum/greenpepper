package com.greenpepper.annotation;

import com.greenpepper.GreenPepper;
import com.greenpepper.expectation.Expectation;
import com.greenpepper.util.log.GreenPepperLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Annotations class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class Annotations
{

    public static final String GREENPEPPER_EXCEPTIONS_LOGGERNAME = "greenpepper.exceptions";
    private static final Logger LOGGER = GreenPepperLogger.getLogger(GREENPEPPER_EXCEPTIONS_LOGGERNAME);
    /**
     * <p>exception.</p>
     *
     * @param t a {@link java.lang.Throwable} object.
     * @return a {@link com.greenpepper.annotation.ExceptionAnnotation} object.
     */
    public static ExceptionAnnotation exception(Throwable t)
    {
		if (t instanceof InvocationTargetException)
		{
            if (GreenPepper.isDebugEnabled()) {
                LOGGER.info("Caught exception in fixture execution", t);
            }
			return new ExceptionAnnotation( ((InvocationTargetException) t).getTargetException() );
		}
		else
		{
            if (GreenPepper.isDebugEnabled()) {
                LOGGER.info("Caught exception in fixture execution", t);
            }
			return new ExceptionAnnotation( t );
		}
	}

    /**
     * <p>right.</p>
     *
     * @return a {@link com.greenpepper.annotation.RightAnnotation} object.
     */
    public static RightAnnotation right()
    {
        return new RightAnnotation();
    }

    /**
     * <p>wrong.</p>
     *
     * @return a {@link com.greenpepper.annotation.WrongAnnotation} object.
     */
    public static WrongAnnotation wrong()
    {
        return new WrongAnnotation();
    }

    /**
     * <p>wrong.</p>
     *
     * @param expected a {@link com.greenpepper.expectation.Expectation} object.
     * @param actual a {@link java.lang.Object} object.
     * @return a {@link com.greenpepper.annotation.WrongAnnotation} object.
     */
    public static WrongAnnotation wrong(Expectation expected, Object actual)
    {
        WrongAnnotation annotation = wrong();
        annotation.giveDetails( expected, actual );
        return annotation;
    }

    /**
     * <p>ignored.</p>
     *
     * @param actual a {@link java.lang.Object} object.
     * @return a {@link com.greenpepper.annotation.IgnoredAnnotation} object.
     */
    public static IgnoredAnnotation ignored(Object actual)
    {
        return new IgnoredAnnotation( actual );
    }

    /**
     * <p>entered.</p>
     *
     * @return a {@link com.greenpepper.annotation.EnteredAnnotation} object.
     */
    public static EnteredAnnotation entered()
    {
        return new EnteredAnnotation();
    }
    
    /**
     * <p>notEntered.</p>
     *
     * @return a {@link com.greenpepper.annotation.NotEnteredAnnotation} object.
     */
    public static NotEnteredAnnotation notEntered()
    {
        return new NotEnteredAnnotation();
    }

    /**
     * <p>missing.</p>
     *
     * @return a {@link com.greenpepper.annotation.Annotation} object.
     */
    public static Annotation missing()
    {
        return new MissingAnnotation();
    }

    /**
     * <p>surplus.</p>
     *
     * @return a {@link com.greenpepper.annotation.SurplusAnnotation} object.
     */
    public static SurplusAnnotation surplus()
    {
        return new SurplusAnnotation();
    }

	/**
	 * <p>skipped.</p>
	 *
	 * @return a {@link com.greenpepper.annotation.SkippedAnnotation} object.
	 */
	public static SkippedAnnotation skipped()
	{
		return new SkippedAnnotation();
	}

	/**
	 * <p>stopped.</p>
	 *
	 * @return a {@link com.greenpepper.annotation.StoppedAnnotation} object.
	 */
	public static StoppedAnnotation stopped()
	{
		return new StoppedAnnotation();
	}

    private Annotations()
    {
    }
}
