/**
 * $Archive: $
 *
 * Copyright 2005 (C) Pyxis Technologies Inc.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Pyxis Technologies inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Pyxis Technologies.
 *
 * http://www.pyxis-tech.com
 */
package com.greenpepper.samples.application.calculator;

/**
 * Demo SUT Application.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Calculator
{
    private int x;
	private int y;

	/**
	 * <p>Getter for the field <code>x</code>.</p>
	 *
	 * @return a int.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * <p>Setter for the field <code>x</code>.</p>
	 *
	 * @param x a int.
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * <p>Getter for the field <code>y</code>.</p>
	 *
	 * @return a int.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * <p>Setter for the field <code>y</code>.</p>
	 *
	 * @param y a int.
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * <p>sum.</p>
	 *
	 * @return a int.
	 */
	public int sum()
    {
        return x + y;
    }

    /**
     * <p>difference.</p>
     *
     * @return a int.
     */
    public int difference()
    {
        return x - y;
    }

    /**
     * <p>product.</p>
     *
     * @return a int.
     */
    public int product()
    {
        return x * y;
    }

    /**
     * <p>quotient.</p>
     *
     * @return a int.
     */
    public int quotient()
    {
        return x / y;
    }
}
