package com.greenpepper.server.domain;

import java.util.Vector;


/**
 * <p>Marshalizable interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Marshalizable
{
    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    Vector<Object> marshallize();
}
