package com.greenpepper.server.license;

import java.util.Date;

/**
 * <p>TimeSource interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface TimeSource
{
    /**
     * <p>now.</p>
     *
     * @return a {@link java.util.Date} object.
     */
    Date now();
}
