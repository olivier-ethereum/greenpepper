package com.greenpepper.server;

/**
 * <p>GreenPepperServerException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperServerException extends Exception
{
    private static final long serialVersionUID = 1L;
    private String id = "";

    /**
     * <p>Constructor for GreenPepperServerException.</p>
     */
    public GreenPepperServerException()
    {
        super();
    }

    /**
     * <p>Constructor for GreenPepperServerException.</p>
     *
     * @param th a {@link java.lang.Throwable} object.
     */
    public GreenPepperServerException(Throwable th)
    {
        super(th);
    }

    /**
     * <p>Constructor for GreenPepperServerException.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param msg a {@link java.lang.String} object.
     */
    public GreenPepperServerException(String id, String msg)
    {
        super(msg);
        this.id = id;
    }

    /**
     * <p>Constructor for GreenPepperServerException.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param msg a {@link java.lang.String} object.
     * @param th a {@link java.lang.Throwable} object.
     */
    public GreenPepperServerException(String id, String msg, Throwable th)
    {
        super(msg, th);
        this.id = id;
    }

	/**
	 * <p>Constructor for GreenPepperServerException.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @param th a {@link java.lang.Throwable} object.
	 */
	public GreenPepperServerException(String id, Throwable th)
	{
		super(th);
		this.id = id;
	}

	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId()
    {
        return this.id;
    }
    
    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.String} object.
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
