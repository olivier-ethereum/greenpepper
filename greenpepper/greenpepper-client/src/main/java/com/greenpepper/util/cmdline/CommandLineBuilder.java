package com.greenpepper.util.cmdline;

import java.io.File;
import java.util.Collection;

import com.greenpepper.server.GreenPepperServerException;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>CommandLineBuilder class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CommandLineBuilder 
{
    private String[] cmdLine;

    /**
     * <p>Constructor for CommandLineBuilder.</p>
     *
     * @param cmdLineTemplate a {@link java.lang.String} object.
     */
    public CommandLineBuilder(String cmdLineTemplate)
    {
        this.cmdLine = cmdLineTemplate.split(" ");
    }

    /**
     * <p>Getter for the field <code>cmdLine</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getCmdLine()
    {
        return cmdLine;
    }
    
    /**
     * <p>setMainClass.</p>
     *
     * @param mainClass a {@link java.lang.String} object.
     */
    public void setMainClass(String mainClass)
    {
        replace("${mainClass}", mainClass);
    }

    /**
     * <p>setInputPath.</p>
     *
     * @param inputPath a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void setInputPath(String inputPath) throws GreenPepperServerException
    {
        replace("${inputPath}", inputPath);
    }
    
    /**
     * <p>setOutpuPath.</p>
     *
     * @param outputPath a {@link java.lang.String} object.
     */
    public void setOutpuPath(String outputPath)
    {
        replace("${outputPath}", outputPath);
    }
    
    /**
     * <p>setDependencies.</p>
     *
     * @param dependencies a {@link java.util.Collection} object.
     */
    public void setDependencies(Collection<String> dependencies)
    {
        StringBuilder sb = new StringBuilder("");
        for(String dependency : dependencies)
        {
            sb.append(dependency);
            sb.append(File.pathSeparator);
        }

        replace("${classpaths}", sb.toString());
    }
    
    /**
     * <p>setSections.</p>
     *
     * @param sections a {@link java.lang.String} object.
     */
    public void setSections(String sections)
    {
        if(!StringUtils.isEmpty(sections))
            addOption("-t", sections);
    }
    
    /**
     * <p>setLocale.</p>
     *
     * @param locale a {@link java.lang.String} object.
     */
    public void setLocale(String locale)
    {
        replace("${locale}", locale);
    }

    /**
     * <p>setRepository.</p>
     *
     * @param repository a {@link java.lang.String} object.
     */
    public void setRepository(String repository)
    {
        replace("${repository}", repository);
    }
    
    /**
     * <p>setFixtureFactory.</p>
     *
     * @param fixtureFactory a {@link java.lang.String} object.
     */
    public void setFixtureFactory(String fixtureFactory)
    {
        replace("${fixtureFactory}", fixtureFactory);
    }

	/**
	 * <p>setProjectDependencyDescriptor.</p>
	 *
	 * @param projectDependencyDescriptor a {@link java.lang.String} object.
	 */
	public void setProjectDependencyDescriptor(String projectDependencyDescriptor)
	{
		replace("${projectDependencyDescriptor}", projectDependencyDescriptor);
	}

	private void addOption(String cmd, String args)
    {
        String[] newCmdLine = new String[cmdLine.length + 2];
        for(int i = 0; i < cmdLine.length; i++)
        {
            newCmdLine[i] = cmdLine[i];
        }
        
        newCmdLine[newCmdLine.length - 2] = cmd;
        newCmdLine[newCmdLine.length - 1] = args;
        cmdLine = newCmdLine;
    }

    private void replace(String name, String value)
    {
        for(int i = 0; i < cmdLine.length; i++)
        {
            if(cmdLine[i].equals(name))
            {
                cmdLine[i] = value == null ? "" : value; 
            }
        }
    }
}
