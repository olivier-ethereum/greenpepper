package com.greenpepper.server.license;

import com.greenpepper.server.GreenPepperServerException;

/**
 * <p>GreenPepperLicenceException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class GreenPepperLicenceException extends GreenPepperServerException
{

    /**
     * <p>Constructor for GreenPepperLicenceException.</p>
     */
    public GreenPepperLicenceException()
    {
        super();
    }

    /**
     * <p>Constructor for GreenPepperLicenceException.</p>
     *
     * @param th a {@link java.lang.Throwable} object.
     */
    public GreenPepperLicenceException(Throwable th)
    {
        super(th);
    }

    /**
     * <p>Constructor for GreenPepperLicenceException.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param msg a {@link java.lang.String} object.
     */
    public GreenPepperLicenceException(String id, String msg)
    {
        super(id, msg);
    }

    /**
     * <p>Constructor for GreenPepperLicenceException.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param msg a {@link java.lang.String} object.
     * @param th a {@link java.lang.Throwable} object.
     */
    public GreenPepperLicenceException(String id, String msg, Throwable th)
    {
        super(id, msg, th);
    }
}
