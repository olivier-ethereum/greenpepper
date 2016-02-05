package com.greenpepper.util.cmdline;

import java.util.List;

/**
 * <p>StreamGobbler interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface StreamGobbler extends Runnable
{
    /**
     * <p>getOutput.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutput();
    /**
     * <p>getError.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getError();

    /**
     * <p>getExceptions.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Exception> getExceptions();
    /**
     * <p>hasErrors.</p>
     *
     * @return a boolean.
     */
    public boolean hasErrors();
    /**
     * <p>hasExceptions.</p>
     *
     * @return a boolean.
     */
    public boolean hasExceptions();
    /**
     * <p>exceptionCaught.</p>
     *
     * @param e a {@link java.lang.Exception} object.
     */
    public void exceptionCaught(Exception e);
}
