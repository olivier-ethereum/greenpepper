package com.greenpepper.util.cmdline;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>CommandLineExecutor class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CommandLineExecutor
{
    private static Logger log = LoggerFactory.getLogger(CommandLineExecutor.class);
    private static final int SUCCESS = 0;
	private StreamGobbler gobbler;
    private String[] cmdLine;
    
    /**
     * <p>Constructor for CommandLineExecutor.</p>
     *
     * @param cmdLine an array of {@link java.lang.String} objects.
     */
    public CommandLineExecutor(String[] cmdLine)
    {
        this.cmdLine = cmdLine;
    }
    
    /**
     * <p>executeAndWait.</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void executeAndWait() throws Exception
    {
        Process p = launchProcess();
        checkForErrors(p);

		if (log.isDebugEnabled())
		{
			// GP-551 : Keep trace of outputs
			if (!StringUtils.isEmpty(getOutput()))
			{
				log.debug("System Output during execution : \n" + getOutput());
			}
			
			if (gobbler.hasErrors())
			{
				log.debug("System Error Output during execution : \n" + gobbler.getError());
			}
		}
    }

    private Process launchProcess() throws Exception
    {
        log.info("Launching cmd: {}", getCmdLineToString());

        Process p = Runtime.getRuntime().exec(cmdLine);
        gobbler = new StreamGobblerImpl(p);
        Thread reader = new Thread(gobbler);
        reader.start();
        p.waitFor();
        return p;
    }

    /**
     * <p>getOutput.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutput()
    {
        return gobbler.getOutput();
    }

    private void checkForErrors(Process p) throws Exception
    {
    	if(p.exitValue() != SUCCESS)
    	{
			if(gobbler.hasErrors())
			{
				throw new Exception(gobbler.getError());
			}
			
			throw new Exception("Process was terminated abnormally");
    	}
    }
    
    private String getCmdLineToString()
    {
        StringBuilder sb = new StringBuilder();
        for(String cmd : cmdLine)
        {
            sb.append(cmd).append(" ");
        }
        
        return sb.toString();
    }
}
