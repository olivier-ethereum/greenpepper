package com.greenpepper.server.domain;

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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_CLASSPATH_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_FIXTURE_CLASSPATH_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_FIXTURE_FACTORY_ARGS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_FIXTURE_FACTORY_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_IS_DEFAULT_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_NAME_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_PROJECT_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUT_RUNNER_IDX;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.StringUtil;

/**
 * SystemUnderTest Class.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="SUT", uniqueConstraints = {@UniqueConstraint(columnNames={"NAME", "PROJECT_ID"})})
@SuppressWarnings("serial")
public class SystemUnderTest extends AbstractUniqueEntity implements Comparable
{
	private static final transient String DEFAULT_JAVA_FIXTURE_FACTORY = DefaultSystemUnderDevelopment.class.getName();
	private static final transient String DEFAULT_DOTNET_FIXTURE_FACTORY = "GreenPepper.Fixtures.PlainOldSystemUnderDevelopment";
	
	private String name;
    private Project project;
    private Runner runner;
	private SortedSet<String> sutClasspaths = new TreeSet<String>();
	private SortedSet<String> fixtureClasspaths = new TreeSet<String>();

    private String fixtureFactory;
    private String fixtureFactoryArgs;

    private byte selected = 0;

	private String projectDependencyDescriptor;

	/**
	 * <p>newInstance.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
	 */
	public static SystemUnderTest newInstance(String name)
    {
        SystemUnderTest sut = new SystemUnderTest();
        sut.setName(name);
        return sut;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "NAME", nullable = false, length=255)
    public String getName()
    {
        return name;
    }

    /**
     * <p>Getter for the field <code>runner</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Runner} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="RUNNER_ID")
    public Runner getRunner()
    {
        return runner;
    }

    /**
     * <p>Getter for the field <code>project</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Project} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="PROJECT_ID")
    public Project getProject()
    {
        return project;
    }

    /**
     * <p>Getter for the field <code>fixtureClasspaths</code>.</p>
     *
     * @return a {@link java.util.SortedSet} object.
     */
    @CollectionOfElements
	@JoinTable( name="SUT_FIXTURE_CLASSPATHS", joinColumns={@JoinColumn(name="SUT_ID")} )
	@Column(name = "elt", nullable = true, length=255)
	@Sort(type = SortType.COMPARATOR, comparator = ClasspathComparator.class)
	public SortedSet<String> getFixtureClasspaths()
    {
        return fixtureClasspaths;
    }

    /**
     * <p>Getter for the field <code>sutClasspaths</code>.</p>
     *
     * @return a {@link java.util.SortedSet} object.
     */
    @CollectionOfElements
	@JoinTable( name="SUT_CLASSPATHS", joinColumns={@JoinColumn(name="SUT_ID")} )
	@Column(name = "elt", nullable = true, length=255)
	@Sort(type = SortType.COMPARATOR, comparator = ClasspathComparator.class)
	public SortedSet<String> getSutClasspaths()
    {
        return sutClasspaths;
    }

    /**
     * <p>Getter for the field <code>fixtureFactory</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "FIXTURE_FACTORY", nullable = true, length=255)
    public String getFixtureFactory()
    {
        return fixtureFactory;
    }

    /**
     * <p>Getter for the field <code>fixtureFactoryArgs</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "FIXTURE_FACTORY_ARGS", nullable = true, length=255)
    public String getFixtureFactoryArgs()
    {
        return fixtureFactoryArgs;
    }

    /**
     * <p>Getter for the field <code>selected</code>.</p>
     *
     * @return a byte.
     */
    @Basic
    @Column(name = "SELECTED")
    public byte getSelected()
    {
        return selected;
    }

	/**
	 * <p>Getter for the field <code>projectDependencyDescriptor</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Basic
	@Column(name = "PROJECT_DEPENDENCY_DESCRIPTOR", nullable = true, length=255)
	public String getProjectDependencyDescriptor()
	{
		return projectDependencyDescriptor;
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
     * <p>Setter for the field <code>runner</code>.</p>
     *
     * @param runner a {@link com.greenpepper.server.domain.Runner} object.
     */
    public void setRunner(Runner runner)
    {
        this.runner = runner;
    }

    /**
     * <p>Setter for the field <code>project</code>.</p>
     *
     * @param project a {@link com.greenpepper.server.domain.Project} object.
     */
    public void setProject(Project project)
    {
        this.project = project;
    }

    /**
     * <p>Setter for the field <code>fixtureClasspaths</code>.</p>
     *
     * @param fixturesClasspaths a {@link java.util.SortedSet} object.
     */
    public void setFixtureClasspaths(SortedSet<String> fixturesClasspaths)
    {
        this.fixtureClasspaths = fixturesClasspaths;
    }

    /**
     * <p>Setter for the field <code>sutClasspaths</code>.</p>
     *
     * @param sutClasspaths a {@link java.util.SortedSet} object.
     */
    public void setSutClasspaths(SortedSet<String> sutClasspaths)
    {
        this.sutClasspaths = sutClasspaths;
    }

    /**
     * <p>Setter for the field <code>fixtureFactory</code>.</p>
     *
     * @param fixtureFactory a {@link java.lang.String} object.
     */
    public void setFixtureFactory(String fixtureFactory)
    {
        this.fixtureFactory = fixtureFactory;
    }

    /**
     * <p>Setter for the field <code>fixtureFactoryArgs</code>.</p>
     *
     * @param fixtureFactoryArgs a {@link java.lang.String} object.
     */
    public void setFixtureFactoryArgs(String fixtureFactoryArgs)
    {
        this.fixtureFactoryArgs = fixtureFactoryArgs;
    }

    /**
     * <p>Setter for the field <code>selected</code>.</p>
     *
     * @param selected a byte.
     */
    public void setSelected(byte selected)
    {
        this.selected = selected;
    }

	/**
	 * <p>Setter for the field <code>projectDependencyDescriptor</code>.</p>
	 *
	 * @param projectDependencyDescriptor a {@link java.lang.String} object.
	 */
	public void setProjectDependencyDescriptor(String projectDependencyDescriptor)
	{
		this.projectDependencyDescriptor = projectDependencyDescriptor;
	}

	/**
	 * <p>isDefault.</p>
	 *
	 * @return a boolean.
	 */
	@Transient
    public boolean isDefault()
    {
        return selected == (byte)1;
    }

    /**
     * <p>setIsDefault.</p>
     *
     * @param isSelected a boolean.
     */
    public void setIsDefault(boolean isSelected)
    {
        this.selected = isSelected ? (byte)1 : (byte)0;
    }

    /**
     * <p>execute.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param implementedVersion a boolean.
     * @param sections a {@link java.lang.String} object.
     * @param locale a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public Execution execute(Specification specification, boolean implementedVersion, String sections, String locale)
    {
        return runner.execute(specification, this, implementedVersion, sections, locale);
    }

    /**
     * <p>fixtureFactoryCmdLineOption.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String fixtureFactoryCmdLineOption()
    {
        if(StringUtil.isEmpty(fixtureFactory)) return getDefaultFixtureFactory();
		if(StringUtil.isEmpty(fixtureFactoryArgs)) return fixtureFactory;
        return fixtureFactory + ";" + fixtureFactoryArgs;
    }

	@Transient
	private String getDefaultFixtureFactory()
	{
		if (runner == null || runner.getEnvironmentType().getName().equals("JAVA"))
		{
			return DEFAULT_JAVA_FIXTURE_FACTORY;
		}
		else
		{
			return DEFAULT_DOTNET_FIXTURE_FACTORY;
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
        parameters.add(SUT_NAME_IDX, name);
        parameters.add(SUT_PROJECT_IDX, project.marshallize());
        parameters.add(SUT_CLASSPATH_IDX, new Vector<String>(sutClasspaths));
        parameters.add(SUT_FIXTURE_CLASSPATH_IDX, new Vector<String>(fixtureClasspaths));
        parameters.add(SUT_FIXTURE_FACTORY_IDX, StringUtils.defaultString(fixtureFactory));
        parameters.add(SUT_FIXTURE_FACTORY_ARGS_IDX, StringUtils.defaultString(fixtureFactoryArgs));
        parameters.add(SUT_IS_DEFAULT_IDX, isDefault());
        parameters.add(SUT_RUNNER_IDX, runner != null ? runner.marshallize() : Runner.newInstance("N/A").marshallize());
		parameters.add(SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX, StringUtils.defaultString(projectDependencyDescriptor));
		return parameters;
    }
    
    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        if(isDefault()) return -1;
        if(((SystemUnderTest)o).isDefault()) return 1;
        return name.compareTo(((SystemUnderTest)o).name);
    }
    
    /**
     * <p>equalsTo.</p>
     *
     * @param o a {@link java.lang.Object} object.
     * @return a boolean.
     */
    public boolean equalsTo(Object o)
    {
        if(o == null || !(o instanceof SystemUnderTest))
        {
            return false;
        }
        
        SystemUnderTest sutCompared = (SystemUnderTest)o;
        if(name == null || !name.equals(sutCompared.getName())) return false;
        if(project == null || !project.equals(sutCompared.getProject())) return false;
        
        return true;
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof SystemUnderTest))
        {
            return false;
        }

        return super.equals(o);
    }
}
