package com.greenpepper.expectation;

/**
 * <p>Either class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Either implements Collator
{
    private Expectation expectation;

    /**
     * <p>Constructor for Either.</p>
     *
     * @param expectation a {@link com.greenpepper.expectation.Expectation} object.
     */
    public Either(Expectation expectation)
    {
        this.expectation = expectation;
    }

    /**
     * <p>or.</p>
     *
     * @param other a {@link com.greenpepper.expectation.Expectation} object.
     * @return a {@link com.greenpepper.expectation.Either} object.
     */
    public Either or(Expectation other)
    {
        this.expectation = new OrExpectation( this.expectation, other );
        return this;
    }

	/**
	 * <p>negate.</p>
	 *
	 * @return a {@link com.greenpepper.expectation.Either} object.
	 */
	public Either negate()
	{
		this.expectation = new NotExpectation( this.expectation );
		return this;
	}

    /**
     * <p>toExpectation.</p>
     *
     * @return a {@link com.greenpepper.expectation.Expectation} object.
     */
    public Expectation toExpectation()
    {
        return expectation;
    }
}
