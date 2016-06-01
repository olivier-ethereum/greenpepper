package com.greenpepper.util;

public class NumberUtilTest
		extends TestCase
{
	public void testCanSubstituteCommaToPeriod() {
		assertEquals("1.1", NumberUtil.substituteDecimalSeparatorToPeriod("1,1"));
	}
}