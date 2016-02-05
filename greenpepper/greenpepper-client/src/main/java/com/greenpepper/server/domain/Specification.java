package com.greenpepper.server.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.TreeSet;
import java.util.SortedSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SPECIFICATION_SUTS_IDX;

/**
 * Specification Class.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="SPECIFICATION", uniqueConstraints = {@UniqueConstraint(columnNames={"NAME", "REPOSITORY_ID"})})
@SuppressWarnings("serial")
public class Specification extends Document
{
    private SortedSet<SystemUnderTest> targetedSystemUnderTests = new TreeSet<SystemUnderTest>();
    protected Set<Reference> references = new HashSet<Reference>();
    private Set<Execution> executions = new HashSet<Execution>();

    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Specification} object.
     */
    public static Specification newInstance(String name)
    {
        Specification specification = new Specification();
        specification.setName(name);
        return specification;
    }
    
    /**
     * <p>Getter for the field <code>targetedSystemUnderTests</code>.</p>
     *
     * @return a {@link java.util.SortedSet} object.
     */
    @ManyToMany( targetEntity= SystemUnderTest.class, cascade={CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinTable( name="SUT_SPECIFICATION", joinColumns={@JoinColumn(name="SPECIFICATION_ID")}, inverseJoinColumns={@JoinColumn(name="SUT_ID")} )
	@Sort(type=SortType.COMPARATOR, comparator=SystemUnderTestByNameComparator.class)
    public SortedSet<SystemUnderTest> getTargetedSystemUnderTests()
    {
        return targetedSystemUnderTests;
    }

    /**
     * <p>Getter for the field <code>executions</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="specification", cascade=CascadeType.ALL)
    public Set<Execution> getExecutions()
    {
        return this.executions;
    }

    /**
     * <p>Getter for the field <code>references</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="specification", cascade=CascadeType.ALL)
    public Set<Reference> getReferences()
    {
        return references;
    }

    /**
     * <p>Setter for the field <code>targetedSystemUnderTests</code>.</p>
     *
     * @param targetedSystemUnderTests a {@link java.util.SortedSet} object.
     */
    public void setTargetedSystemUnderTests(SortedSet<SystemUnderTest> targetedSystemUnderTests)
    {
		this.targetedSystemUnderTests = targetedSystemUnderTests;
    }

    /**
     * <p>Setter for the field <code>executions</code>.</p>
     *
     * @param executions a {@link java.util.Set} object.
     */
    public void setExecutions(Set<Execution> executions)
    {
        this.executions = executions;
    }
    
    /**
     * <p>Setter for the field <code>references</code>.</p>
     *
     * @param references a {@link java.util.Set} object.
     */
    public void setReferences(Set<Reference> references)
    {
        this.references = references;
    }

    /**
     * <p>addSystemUnderTest.</p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public void addSystemUnderTest(SystemUnderTest systemUnderTest)
    {
        targetedSystemUnderTests.add(systemUnderTest);
    }

    /**
     * <p>addExecution.</p>
     *
     * @param execution a {@link com.greenpepper.server.domain.Execution} object.
     */
    public void addExecution(Execution execution)
    {
        execution.setSpecification(this);
        executions.add(execution);
    }

    /**
     * <p>removeSystemUnderTest.</p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest)
    {
        targetedSystemUnderTests.remove(systemUnderTest);
        if(targetedSystemUnderTests.isEmpty())
            addSystemUnderTest(getRepository().getProject().getDefaultSystemUnderTest());
    }

    /**
     * <p>removeReference.</p>
     *
     * @param reference a {@link com.greenpepper.server.domain.Reference} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    public void removeReference(Reference reference) throws GreenPepperServerException
    {
        if(!references.contains(reference))
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REFERENCE_NOT_FOUND, "Reference not found");
        }

        references.remove(reference);
        reference.setSpecification(null);
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = super.marshallize();
        Vector<Object> suts = XmlRpcDataMarshaller.toXmlRpcSystemUnderTestsParameters(targetedSystemUnderTests);
        parameters.add(SPECIFICATION_SUTS_IDX, suts);
        return parameters;
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
		return super.equals(o) && o instanceof Specification;
	}
}
