package com.greenpepper.util;

/**
 * <p>NumberUtil class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class NumberUtil
{
	private NumberUtil()
	{

	}

	/**
	 * <p>substituteDecimalSeparatorToPeriod.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String substituteDecimalSeparatorToPeriod(String value)
	{
		if (StringUtil.isBlank(value))
		{
			return value;
		}

		int commaPosition = value.lastIndexOf(',');

		if (commaPosition == -1)
		{
			return value;
		}

		return value.substring(0, commaPosition) + "." +
			   value.substring(commaPosition + 1);
	}
}
