package com.greenpepper.agent.server;

/**
 * <p>ComandLineHelper class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ComandLineHelper
{

	private static String SECURED = "-secured";
	private static String PORT = "-port";
	private static String KEYSTORE = "-keystore";
	private static String CONFIG = "-config";

	private String args[];

	/**
	 * <p>Constructor for ComandLineHelper.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public ComandLineHelper(String args[])
	{
		this.args = args;
	}

	/**
	 * <p>getConfig.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getConfig()
	{
		return getParameterValue(CONFIG);
	}

	/**
	 * <p>isSecured.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSecured()
	{
		for (String arg : args)
		{
			if (SECURED.equalsIgnoreCase(arg))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>getPort.</p>
	 *
	 * @param defaultPort a int.
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getPort(int defaultPort)
	{
		String port = getParameterValue(PORT);

		try
		{
			if (port != null)
			{
				return Integer.parseInt(port);
			}
		}
		catch (Exception ex)
		{

		}
		
		return defaultPort;
	}

	/**
	 * <p>getKeyStore.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKeyStore()
	{
		return getParameterValue(KEYSTORE);
	}

	private String getParameterValue(String parameter)
	{
		if (args == null)
		{
			return null;
		}

		for (int i = 0; i < args.length; i++)
		{
			if (parameter.equalsIgnoreCase(args[i]))
			{
				if (i + 1 < args.length)
				{
					return args[i + 1];
				}
				else
				{
					return null;
				}
			}
		}

		return null;
	}
}
