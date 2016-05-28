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
public class Division
{
    public double dividend;
    public double divisor;

    /**
     * <p>quotient.</p>
     *
     * @return a double.
     */
    public double quotient()
    {
        return dividend / divisor;
    }
}
