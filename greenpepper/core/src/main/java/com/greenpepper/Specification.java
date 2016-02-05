package com.greenpepper;

/**
 * <p>Specification interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Specification extends ExecutionContext
{
    /**
     * <p>nextExample.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    Example nextExample();

    /**
     * <p>hasMoreExamples.</p>
     *
     * @return a boolean.
     */
    boolean hasMoreExamples();

    /**
     * <p>exampleDone.</p>
     *
     * @param statistics a {@link com.greenpepper.Statistics} object.
     */
    void exampleDone( Statistics statistics );
}
