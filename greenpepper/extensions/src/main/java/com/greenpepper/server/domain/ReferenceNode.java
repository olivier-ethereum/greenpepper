package com.greenpepper.server.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Vector;

/**
 * <p>ReferenceNode class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ReferenceNode extends DocumentNode implements Marshalizable
{
	private String repositoryUID;
	private String sutName;
	private String section;

	/**
	 * <p>Constructor for ReferenceNode.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @param repositoryUID a {@link java.lang.String} object.
	 * @param sutName a {@link java.lang.String} object.
	 * @param section a {@link java.lang.String} object.
	 */
	public ReferenceNode(String title, String repositoryUID, String sutName, String section) 
	{
		super(title);
		this.repositoryUID = repositoryUID;
		this.sutName = sutName;
		this.section = section;
	}

	/**
	 * <p>Getter for the field <code>repositoryUID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRepositoryUID() 
	{
		return repositoryUID;
	}

	/**
	 * <p>Getter for the field <code>sutName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSutName() 
	{
		return sutName;
	}

	/**
	 * <p>Getter for the field <code>section</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSection() 
	{
		return section;
	}

	/** {@inheritDoc} */
	@Override
	public void addChildren(DocumentNode child) 
	{
		throw new RuntimeException("Reference node should not have children");
	}

	/** {@inheritDoc} */
	@Override
	public Vector<Object> marshallize() 
	{
		Vector<Object> vector = super.marshallize();
		vector.add(NODE_REPOSITORY_UID_INDEX, repositoryUID);
		vector.add(NODE_SUT_NAME_INDEX, sutName);
		vector.add(NODE_SECTION_INDEX, StringUtils.defaultString(section));
		return vector;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) 
	{
        if(o == null || !(o instanceof ReferenceNode))
        {
            return false;
        }

        ReferenceNode nodeCompared = (ReferenceNode)o;
        if(super.equals(o))
        {
        	return getRepositoryUID().equals(nodeCompared.getRepositoryUID()) && getSutName().equals(nodeCompared.getSutName());
        }

        return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() 
	{
		return super.hashCode() + getRepositoryUID().hashCode() + getSutName().hashCode();
	}
}
