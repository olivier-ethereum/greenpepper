package com.greenpepper.confluence.tools;

import static com.greenpepper.util.URIUtil.decoded;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.apache.xmlrpc.XmlRpcException;

import com.greenpepper.GreenPepperCore;
import com.greenpepper.util.cli.ArgumentMissingException;
import com.greenpepper.util.cli.CommandLine;
import com.greenpepper.util.cli.ParseException;

/**
 * <p>GetSpec class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GetSpec {

	/** Constant <code>URI="http://localhost:8090"</code> */
	public static final String URI = "http://localhost:8090";
	/** Constant <code>RPC_PATH="/rpc/xmlrpc"</code> */
	public static final String RPC_PATH = "/rpc/xmlrpc";
	private static final Vector EMPTY = new Vector(0);
	private static final Object USER_NAME = "admin";
	private static final Object PASSWORD = "admin";

	private CommandLine cli;
	
	/**
	 * <p>Constructor for GetSpec.</p>
	 */
	public GetSpec() {
		this.cli = new CommandLine();
	}
	
	/**
	 * <p>run.</p>
	 *
	 * @param args a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	public void run(String... args) throws Exception {
		defineCommandLine();
		
		if (!parseCommandLine( args )) return;
		
		getSpecification();
	}
	
    private boolean parseCommandLine(String[] args)  throws ParseException {
        cli.parse( args );
        if (optionSpecified( "help" )) return displayUsage();
        if (optionSpecified( "version" )) return displayVersion();
        if (confluenceUrl() == null) throw new ArgumentMissingException( "confluence_url" );
        return true;
	}

    private String confluenceUrl()
    {
        return cli.getArgument( 0 ) != null ? decoded( cli.getArgument( 0 ) ) : URI;
    }
    
    private boolean optionSpecified(String name)
    {
        return cli.hasOptionValue( name );
    }
    
	private void getSpecification() {
		try {
			String xmlRpcUrl = getXmlRpcURL();
			System.out.println("GettingSpecification : " + xmlRpcUrl);
			
			XmlRpcClient rpcClient = new XmlRpcClientLite(xmlRpcUrl);
			
			Vector params = new Vector(3);
			params.add(USER_NAME);
			params.add(PASSWORD);

			Vector specs = new Vector(2);
			specs.add("GREENPEPPERDEMO");
			specs.add("Bank");
			specs.add(Boolean.FALSE);
			params.add(specs);
			String specification = (String)rpcClient.execute(buildRequest("getRenderedSpecification"), params);
			System.err.println(specification);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlRpcException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getXmlRpcURL() {
		return  confluenceUrl() + RPC_PATH;
	}

	private void defineCommandLine()
    {
        String banner = "com.greenpepper.confluence.tools.ListRepo [options] confluence_url\n" +
                "List all the repositories found in the specified confluence ";
        cli.setBanner( banner );

        cli.defineOption( cli.buildOption( "help", "--help", "Display this help and exit" ) );
        cli.defineOption( cli.buildOption( "version", "--version", "Output version information and exit" ) );
    }

    private boolean displayVersion()
    {
        System.out.println( String.format( "GreenPepper version \"%s\"", GreenPepperCore.VERSION ) );
        return false;
    }

    private boolean displayUsage()
    {
    	System.out.println( cli.usage() );
        return false;
    }

	private static String buildRequest(String methodName) {
		return "greenpepper1." + methodName;
	}

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public static void main(String[] args) {

		GetSpec listRepo = new GetSpec();
		try {
			listRepo.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
