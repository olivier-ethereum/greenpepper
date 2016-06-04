/*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.samples.application.mortgage;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FinanceableAmountSpecificationTest {
    private FinanceableAmountSpecification specification;

    @Before
    public void setUp()
    {
        specification = new FinanceableAmountSpecification();
    }


    @Test
    public void testThatAPropertyWithoutValueCanNotBeFinanced() {
        Property zeroValueProperty = Property.valued(Money.zero());

        assertEquals(Money.zero(), specification.financeableAmmountOf(zeroValueProperty));
    }

    @Test
    public void testThatTheFinanceableAmountIsProportionalToPropertyValue()
    {
        Property chalet = Property.valued(Money.parse("$100 000"));
        assertEquals(Money.parse("$75,000"), specification.financeableAmmountOf(chalet));
    }


}
