package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;
import static com.greenpepper.util.IOUtils.uniquePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.report.XmlReport;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import com.greenpepper.server.rpc.xmlrpc.client.XmlRpcClientExecutor;
import com.greenpepper.server.rpc.xmlrpc.client.XmlRpcClientExecutorFactory;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.ExceptionUtils;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.StringUtil;
import com.greenpepper.util.URIUtil;
import com.greenpepper.util.cmdline.CommandLineBuilder;
import com.greenpepper.util.cmdline.CommandLineExecutor;

/**
 * Runner Class.
 * Definition of a Runner.
 * <p/>
 * Copyright (c) 2006-2007 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="RUNNER")
@SuppressWarnings("serial")
public class Runner extends AbstractVersionedEntity implements Comparable
{

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);
	private static final String AGENT_HANDLER = "greenpepper-agent1";
    private String name;
    private String cmdLineTemplate;
    private String mainClass;
    private EnvironmentType envType;
    
    private String serverName;
    private String serverPort;
    private Boolean secured;
    
    private SortedSet<String> classpaths = new TreeSet<String>();
    
    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Runner} object.
     */
    public static Runner newInstance(String name)
    {
        Runner runner = new Runner();
        runner.setName(name);
        
        return runner;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length=255)
    public String getName()
    {
        return name;
    }

    /**
     * <p>getEnvironmentType.</p>
     *
     * @return a {@link com.greenpepper.server.domain.EnvironmentType} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="ENVIRONMENT_TYPE_ID")
    public EnvironmentType getEnvironmentType()
    {
        return envType;
    }

    /**
     * <p>Getter for the field <code>serverName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "SERVER_NAME", nullable = true, length=255)
    public String getServerName()
    {
        return serverName;
    }
    
    /**
     * <p>Getter for the field <code>serverPort</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "SERVER_PORT", nullable = true, length=8)
    public String getServerPort()
    {
        return serverPort;
    }

    /**
     * <p>isSecured.</p>
     *
     * @return a boolean.
     */
    @Basic
    @Column(name = "SECURED", nullable = true)
    public boolean isSecured()
    {
    	return secured != null && secured.booleanValue();
    }
    
    /**
     * <p>Getter for the field <code>cmdLineTemplate</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "CMD_LINE_TEMPLATE", nullable = true, length=510)
    public String getCmdLineTemplate()
    {
        return cmdLineTemplate;
    }

    /**
     * <p>Getter for the field <code>mainClass</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "MAIN_CLASS", nullable = true, length=255)
    public String getMainClass()
    {
        return mainClass;
    }

    /**
     * <p>Getter for the field <code>classpaths</code>.</p>
     *
     * @return a {@link java.util.SortedSet} object.
     */
    @CollectionOfElements
	@JoinTable( name="RUNNER_CLASSPATHS", joinColumns={@JoinColumn(name="RUNNER_ID")} )
	@Column(name = "elt", nullable = true, length=255)
	@Sort(type = SortType.COMPARATOR, comparator = ClasspathComparator.class)
	public SortedSet<String> getClasspaths()
    {
        return classpaths;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * <p>setEnvironmentType.</p>
     *
     * @param envType a {@link com.greenpepper.server.domain.EnvironmentType} object.
     */
    public void setEnvironmentType(EnvironmentType envType)
    {
        this.envType = envType;
    }

    /**
     * <p>Setter for the field <code>serverName</code>.</p>
     *
     * @param serverName a {@link java.lang.String} object.
     */
    public void setServerName(String serverName)
    {
        this.serverName = StringUtil.toNullIfEmpty(serverName);
    }

    /**
     * <p>Setter for the field <code>serverPort</code>.</p>
     *
     * @param serverPort a {@link java.lang.String} object.
     */
    public void setServerPort(String serverPort)
    {
        this.serverPort = StringUtil.toNullIfEmpty(serverPort);
    }
    
    /**
     * <p>Setter for the field <code>secured</code>.</p>
     *
     * @param secured a {@link java.lang.Boolean} object.
     */
    public void setSecured(Boolean secured)
    {
        this.secured = secured != null && secured.booleanValue();
    }

    /**
     * <p>Setter for the field <code>cmdLineTemplate</code>.</p>
     *
     * @param cmdLineTemplate a {@link java.lang.String} object.
     */
    public void setCmdLineTemplate(String cmdLineTemplate)
    {
        this.cmdLineTemplate = StringUtil.toNullIfEmpty(cmdLineTemplate);
    }
    
    /**
     * <p>Setter for the field <code>mainClass</code>.</p>
     *
     * @param mainClass a {@link java.lang.String} object.
     */
    public void setMainClass(String mainClass)
    {
        this.mainClass = StringUtil.toNullIfEmpty(mainClass);
    }

    /**
     * <p>Setter for the field <code>classpaths</code>.</p>
     *
     * @param classpaths a {@link java.util.SortedSet} object.
     */
    public void setClasspaths(SortedSet<String> classpaths)
    {
        this.classpaths = classpaths;
    }

    /**
     * <p>execute.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param implementedVersion a boolean.
     * @param sections a {@link java.lang.String} object.
     * @param locale a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public Execution execute(Specification specification, SystemUnderTest systemUnderTest, boolean implementedVersion, String sections, String locale)
    {
    	if(isRemote())
    	{
    		return executeRemotely(specification, systemUnderTest, implementedVersion, sections, locale);
    	}
    	else
    	{
    		return executeLocally(specification, systemUnderTest, implementedVersion, sections, locale);
    	}
    }
    
    @SuppressWarnings("unchecked")
    protected Execution executeRemotely(Specification specification, SystemUnderTest systemUnderTest, boolean implementedVersion, String sections, String locale)
    {
        LOG.debug("Execute Remotely {} on agentURL {}", specification.getName(), agentUrl());

        try
        {
        	sections = (String) StringUtils.defaultString(sections);
        	locale = (String)StringUtils.defaultString(locale);

	        XmlRpcClientExecutor xmlrpc = XmlRpcClientExecutorFactory.newExecutor(agentUrl());

	        Vector params = CollectionUtil.toVector(marshallize(), systemUnderTest.marshallize(), specification.marshallize(), implementedVersion, sections, locale);

	        Vector<Object> execParams = (Vector<Object>)xmlrpc.execute(AGENT_HANDLER+".execute", params);
	        
			Execution execution = XmlRpcDataMarshaller.toExecution(execParams);
	        execution.setSystemUnderTest(systemUnderTest);
	        execution.setSpecification(specification);
			execution.setRemotelyExecuted();
			return execution;
        }
        catch (Exception e)
        {
            return Execution.error(specification, systemUnderTest, sections, ExceptionUtils.stackTrace(e, "<br>", 15));
        }
    }
    
    private Execution executeLocally(Specification specification, SystemUnderTest systemUnderTest, boolean implementedVersion, String sections, String locale)
    {
        String outpuPath = null;
        try {
            outpuPath = uniquePath("GreenPepperTest", ".tst");
            return executeLocally(specification, systemUnderTest, implementedVersion, sections, locale, outpuPath);
        } catch (IOException e) {
            return Execution.error(specification, systemUnderTest, sections, ExceptionUtils.stackTrace(e, "<br>", 15));
        }

    }

    protected Execution executeLocally(Specification specification, SystemUnderTest systemUnderTest, boolean implementedVersion, String sections, String locale, String outpuPath) {

        File outputFile = null;
        try
        {
            outputFile = new File(outpuPath);

            String[] cmdLine = compileCmdLine(specification, systemUnderTest, outpuPath, implementedVersion, sections, locale);
            new CommandLineExecutor(cmdLine).executeAndWait();

            return Execution.newInstance(specification, systemUnderTest, XmlReport.parse(outputFile));}
        catch (GreenPepperServerException e)
        {
            return Execution.error(specification, systemUnderTest, sections, e.getId());
        }
        catch (Exception e)
        {
            return Execution.error(specification, systemUnderTest, sections, ExceptionUtils.stackTrace(e, "<br>", 15));
        }
        finally
        {
            IOUtil.deleteFile(outputFile);
        }
    }

    /**
	 * <p>marshallize.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(RUNNER_NAME_IDX, name);
        parameters.add(RUNNER_CMDLINE_IDX, StringUtils.defaultString(cmdLineTemplate));
        parameters.add(RUNNER_ENVTYPE_IDX, envType != null ? envType.marshallize() : EnvironmentType.newInstance("").marshallize());
        parameters.add(RUNNER_SERVER_NAME_IDX, StringUtils.defaultString(serverName));
        parameters.add(RUNNER_SERVER_PORT_IDX, StringUtils.defaultString(serverPort));
        parameters.add(RUNNER_MAINCLASS_IDX, StringUtils.defaultString(mainClass));
        parameters.add(RUNNER_CLASSPATH_IDX, new Vector<String>(classpaths));
        parameters.add(RUNNER_SECURED_IDX, isSecured());
        return parameters;
    }

    /**
     * <p>agentUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String agentUrl() 
	{
		return ( isSecured() ? "https://" : "http://" ) + serverName + ":" + serverPort;
	}
    
    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return this.getName().compareTo(((Runner)o).getName());
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Runner))
        {
            return false;
        }

        Runner runnerCompared = (Runner)o;
		return getName().equals(runnerCompared.getName());
	}

    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return getName().hashCode();
    }
    
    private String[] compileCmdLine(Specification spec, SystemUnderTest sut, String outpuPath, boolean implementedVersion, String sections, String locale) throws Exception
    {
        CommandLineBuilder cmdBuilder = new CommandLineBuilder(cmdLineTemplate);
        cmdBuilder.setDependencies(mergedDependencies(sut));
        cmdBuilder.setMainClass(mainClass);
        cmdBuilder.setInputPath(URIUtil.raw(spec.getName()) + (implementedVersion ? "" : "?implemented=false"));
        cmdBuilder.setOutpuPath(outpuPath);
        cmdBuilder.setRepository(spec.getRepository().asCmdLineOption(envType));
        cmdBuilder.setFixtureFactory(sut.fixtureFactoryCmdLineOption());
		cmdBuilder.setProjectDependencyDescriptor(sut.getProjectDependencyDescriptor());
		cmdBuilder.setSections(sections);
        cmdBuilder.setLocale(locale);
        
        return cmdBuilder.getCmdLine();
    }

    private Collection<String> mergedDependencies(SystemUnderTest systemUnderTest)
    {
        Collection<String> dependencies = new ArrayList<String>();
        dependencies.addAll(getClasspaths());
        dependencies.addAll(systemUnderTest.getFixtureClasspaths());
        dependencies.addAll(systemUnderTest.getSutClasspaths());
        return dependencies;
    }
    
    @Transient
    protected boolean isRemote()
    {
    	return !StringUtil.isEmpty(serverName) && !StringUtil.isEmpty(serverPort);
    }
}
