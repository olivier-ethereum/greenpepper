package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_LAST_EXECUTION_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_REQUIREMENT_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_SECTIONS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_SPECIFICATION_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_SUT_IDX;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import com.greenpepper.util.StringUtil;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * TestCase Class.
 * Main association class between a requirement, a test docuement and a system under test.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="REFERENCE", uniqueConstraints = {@UniqueConstraint(columnNames={"REQUIREMENT_ID", "SPECIFICATION_ID", "SUT_ID", "SECTIONS"})})
@SuppressWarnings("serial")
public class Reference extends AbstractUniqueEntity implements Comparable
{
    private String sections;
    private Requirement requirement;
    private Specification specification;
    private SystemUnderTest systemUnderTest;
    private Execution lastExecution;

    /**
     * <p>newInstance.</p>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @return a {@link com.greenpepper.server.domain.Reference} object.
     */
    public static Reference newInstance(Requirement requirement, Specification specification, SystemUnderTest sut)
    {
        return newInstance(requirement, specification, sut, null);
    }

    /**
     * <p>newInstance.</p>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param sut a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param sections a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Reference} object.
     */
    public static Reference newInstance(Requirement requirement, Specification specification, SystemUnderTest sut, String sections)
    {
        Reference reference = new Reference();
        reference.setSections(sections);

        reference.setRequirement(requirement);
        reference.setSpecification(specification);
        reference.setSystemUnderTest(sut);
        requirement.getReferences().add(reference);
        specification.getReferences().add(reference);

        return reference;
    }

    /**
     * <p>Getter for the field <code>sections</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "SECTIONS", nullable = true, length=50)
    public String getSections()
    {
        return sections;
    }

    /**
     * <p>Getter for the field <code>requirement</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Requirement} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.ALL} )
    @JoinColumn(name="REQUIREMENT_ID")
    public Requirement getRequirement()
    {
        return requirement;
    }

    /**
     * <p>Getter for the field <code>specification</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Specification} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="SPECIFICATION_ID")
    public Specification getSpecification()
    {
        return specification;
    }

    /**
     * <p>Getter for the field <code>systemUnderTest</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="SUT_ID")
    public SystemUnderTest getSystemUnderTest()
    {
        return systemUnderTest;
    }
    
    /**
     * <p>Getter for the field <code>lastExecution</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="LAST_EXECUTION_ID")
    public Execution getLastExecution()
    {
        return lastExecution;
    }
    
    /**
     * <p>Setter for the field <code>sections</code>.</p>
     *
     * @param sections a {@link java.lang.String} object.
     */
    public void setSections(String sections)
    {
        this.sections = StringUtil.toNullIfEmpty(sections);
    }

    /**
     * <p>Setter for the field <code>requirement</code>.</p>
     *
     * @param requirement a {@link com.greenpepper.server.domain.Requirement} object.
     */
    public void setRequirement(Requirement requirement)
    {
        this.requirement = requirement;
    }

    /**
     * <p>Setter for the field <code>specification</code>.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     */
    public void setSpecification(Specification specification)
    {
        this.specification = specification;
    }

    /**
     * <p>Setter for the field <code>systemUnderTest</code>.</p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public void setSystemUnderTest(SystemUnderTest systemUnderTest)
    {
        this.systemUnderTest = systemUnderTest;
    }

    /**
     * <p>Setter for the field <code>lastExecution</code>.</p>
     *
     * @param lastExecution a {@link com.greenpepper.server.domain.Execution} object.
     */
    public void setLastExecution(Execution lastExecution)
    {
        this.lastExecution = lastExecution;
    }
    
    /**
     * <p>getStatus.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Transient
    public String getStatus()
    {
        return lastExecution != null ? lastExecution.getStatus() : Execution.IGNORED; 
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();        
        parameters.add(REFERENCE_REQUIREMENT_IDX, requirement.marshallize());
        parameters.add(REFERENCE_SPECIFICATION_IDX, specification.marshallize());
        parameters.add(REFERENCE_SUT_IDX, systemUnderTest.marshallize());
        parameters.add(REFERENCE_SECTIONS_IDX, XmlRpcDataMarshaller.padNull(sections));
        
        parameters.add(REFERENCE_LAST_EXECUTION_IDX, lastExecution != null ? lastExecution.marshallize() : Execution.none().marshallize());
        return parameters;
    }
    
    /**
     * <p>execute.</p>
     *
     * @param implementedVersion a boolean.
     * @param locale a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public Execution execute(boolean implementedVersion, String locale)
    {
        return systemUnderTest.execute(specification, implementedVersion, sections, locale);
    }

    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        Reference referenceCompared = (Reference)o;
        int compare = specification.compareTo(referenceCompared.specification);
        if(compare != 0)
        {
            return compare;
        }

        compare = requirement.compareTo(referenceCompared.requirement);
        if(compare != 0)
        {
            return compare;
        }

        compare = systemUnderTest.compareTo(referenceCompared.systemUnderTest);
        if(compare != 0)
        {
            return compare;
        }

		return StringUtil.compare(sections, referenceCompared.sections);
	}
    
    /**
     * <p>equalsTo.</p>
     *
     * @param o a {@link java.lang.Object} object.
     * @return a boolean.
     */
    public boolean equalsTo(Object o)
    {
        if(o == null || !(o instanceof Reference))
        {
            return false;
        }
        
        Reference refCompared = (Reference)o;
        if(!StringUtil.isEquals(sections, refCompared.sections)) return false;
        if(systemUnderTest == null || !systemUnderTest.equalsTo(refCompared.systemUnderTest)) return false;
        if(requirement == null || !requirement.equalsTo(refCompared.requirement)) return false;
        if(specification == null || !specification.equalsTo(refCompared.specification)) return false;
        
        return true;
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Reference))
        {
            return false;
        }

        return super.equals(o);
    }
}
