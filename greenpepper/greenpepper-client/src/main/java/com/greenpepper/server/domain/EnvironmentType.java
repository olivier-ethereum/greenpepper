package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.ENVTYPE_NAME_IDX;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
/**
 * <p>EnvironmentType class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="ENVIRONMENT_TYPE")
@SuppressWarnings("serial")
public class EnvironmentType extends AbstractEntity implements Comparable<EnvironmentType>
{
	private String name;
	
    /**
     * <p>newInstance.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.EnvironmentType} object.
     */
    public static EnvironmentType newInstance(String name)
    {
    	EnvironmentType env = new EnvironmentType();
    	env.setName(name);
        return env;
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
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name)
    {
        this.name = name;
    }

	/**
	 * <p>marshallize.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> marshallize()
	{
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(ENVTYPE_NAME_IDX, StringUtils.defaultString(name));
		return parameters;
	}

    /**
     * <p>compareTo.</p>
     *
     * @param envCompared a {@link com.greenpepper.server.domain.EnvironmentType} object.
     * @return a int.
     */
    public int compareTo(EnvironmentType envCompared)
    {
        return this.getName().compareTo(envCompared.getName());
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof EnvironmentType))
        {
            return false;
        }

        EnvironmentType envCompared = (EnvironmentType)o;
        return getName().equals(envCompared.getName());
    }

    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
		return getName() == null ? 0 : getName().hashCode();
    }
}
