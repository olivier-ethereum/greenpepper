package com.greenpepper.agent.server;

import java.util.Scanner;
import java.nio.charset.Charset;

import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.secure.SecureWebServer;
import org.apache.xmlrpc.secure.SecurityTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Agent
{

	private static final Logger log = LoggerFactory.getLogger(Agent.class);

	private static WebServer webServer;

	public static void main(String[] args)
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				log.info("Shutting down GreenPepper Remote Agent...");
				shutdown();
			}
		});
		
		try
		{
			AgentConfiguration configuration = new AgentConfiguration(args);

			if (configuration.isSecured())
			{
				SecurityTool.setKeyStore(configuration.getKeyStore());
				SecurityTool.setKeyStorePassword(configuration.getKeyStorePassword() == null ?
												 askPassword() : configuration.getKeyStorePassword());

				webServer = new SecureWebServer(configuration.getPort());
			}
			else
			{
				webServer = new WebServer(configuration.getPort());
			}

			Service service = new ServiceImpl();
			webServer.addHandler("greenpepper-agent1", service);
			webServer.start();

			log.info(String.format("GreenPepper Remote Agent is listening for %sconnections on port %s... ",
								   configuration.isSecured() ? "SSL " : "", configuration.getPort()));

			log.info(String.format("File encoding : %s, Charset : %s",
								   System.getProperty("file.encoding"),
								   Charset.defaultCharset().toString()));
		}
		catch (Exception ex)
		{
			log.error("Error starting GreenPepper Remote Agent", ex);
		}
	}

	private static String askPassword()
	{
		System.out.println("Enter keystore password : ");

		// JDK1.6 SPECIFIC : return String.valueOf(System.console().readPassword("Enter keystore password : "));

		Scanner in = new Scanner(System.in);

		try
		{
			return in.nextLine();
		}
		finally
		{
			in.close();
		}
	}

	public static void shutdown()
	{
		if (webServer != null)
		{
			webServer.shutdown();
			webServer = null;
		}
	}
}