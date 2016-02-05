package com.greenpepper.server.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;

/**
 * Requirement Class.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */

@Entity
@Table(name="REQUIREMENT", uniqueConstraints = {@UniqueConstraint(columnNames={"NAME", "REPOSITORY_ID"})})
@SuppressWarnings("serial")
public class Requirement extends Document
{
    protected Set<Reference> references = new HashSet<Reference>();
    
    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Requirement} object.
     */
    public static Requirement newInstance(String name)
    {
        Requirement requirement = new Requirement();
        requirement.setName(name);
        return requirement;
    }

    /**
     * <p>Getter for the field <code>references</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    @OneToMany(mappedBy="requirement", cascade=CascadeType.ALL)
    public Set<Reference> getReferences()
    {
        return references;
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
        reference.setRequirement(null);
    }
    
    /**
     * <p>getSummary.</p>
     *
     * @return a {@link com.greenpepper.server.domain.RequirementSummary} object.
     */
    @Transient
    public RequirementSummary getSummary()
    {
        RequirementSummary summary = new RequirementSummary();
        for(Reference ref : references)
        {
            if(ref.getLastExecution() != null)
            {
                summary.addErrors(ref.getLastExecution().getErrors());
                summary.addException(ref.getLastExecution().hasException());
                summary.addFailures(ref.getLastExecution().getFailures());
                summary.addSuccess(ref.getLastExecution().getSuccess());
                summary.addErrors(ref.getLastExecution().getErrors());
            }
        }
        
        summary.setReferencesSize(references.size());
        return summary;
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if (super.equals(o))
        {
            return o instanceof Requirement;
        }

        return false;
    }
}
