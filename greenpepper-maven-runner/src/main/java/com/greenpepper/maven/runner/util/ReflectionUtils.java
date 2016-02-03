/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
package com.greenpepper.maven.runner.util;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public final class ReflectionUtils
{
    private ReflectionUtils() {}

	public static Object getDeclaredFieldValue(Object object, String declaredFieldName)
			throws Exception
	{
		Field field = object.getClass().getDeclaredField(declaredFieldName);

		field.setAccessible(true);

		return field.get(object);
	}

	public static void invokeMain(Class<?> mainClass, List<String> args)
			throws Exception
	{
		Method mainMethod = mainClass.getMethod("main", Class.forName("[Ljava.lang.String;"));

		mainMethod.invoke(null, convertToArray(args));
	}

    public static void setDebugEnabled(ClassLoader classLoader, boolean isDebug)
            throws Exception
    {
        Class<?> greenPepperClass = classLoader.loadClass("com.greenpepper.GreenPepper");
        Method setDebugEnabledMethod = greenPepperClass.getMethod("setDebugEnabled", boolean.class);
        setDebugEnabledMethod.invoke( null, isDebug );
    }

	public static void setSystemOutputs(ClassLoader classLoader, PrintStream out, PrintStream err)
			throws Exception
	{
		Class<?> systemClass = classLoader.loadClass("java.lang.System");
		Method setSystemOutMethod = systemClass.getMethod("setOut", PrintStream.class);
		setSystemOutMethod.invoke(null, out);
		Method setSystemErrMethod = systemClass.getMethod("setErr", PrintStream.class);
		setSystemErrMethod.invoke(null, err);
	}

	private static Object convertToArray(List<String> args)
	{
		String[] array = new String[args.size()];
		args.toArray(array);
		return array;
	}
}