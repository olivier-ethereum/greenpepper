package com.greenpepper.server.domain.component;

import javax.persistence.Embeddable;

@Embeddable
/**
 * <p>ContentType class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ContentType
{
    /** Constant <code>TEST</code> */
    public final static ContentType TEST = new ContentType("TEST");
    /** Constant <code>REQUIREMENT</code> */
    public final static ContentType REQUIREMENT = new ContentType("REQUIREMENT");
    /** Constant <code>BOTH</code> */
    public final static ContentType BOTH = new ContentType("BOTH");
    /** Constant <code>UNKNOWN</code> */
    public final static ContentType UNKNOWN = new ContentType("UNKNOWN");

    private String contentType;

    private ContentType()
    {
    }

    private ContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * <p>getInstance.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.component.ContentType} object.
     */
    public static ContentType getInstance(String type)
    {
        if(type.equals(TEST.toString()))
        {
            return TEST;
        }
        else if(type.equals(REQUIREMENT.toString()))
        {
            return REQUIREMENT;
        }
        else if(type.equals(BOTH.toString()))
        {
            return BOTH;
        }

        return UNKNOWN;
    }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ContentType && getContentType().equals(((ContentType)obj).getContentType());
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
    {
        return this.contentType;
    }

    @SuppressWarnings("unused")
    private String getContentType()
    {
        return this.contentType;
    }

    @SuppressWarnings("unused")
    private void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * <p>containsSpecifications.</p>
     *
     * @param type a {@link com.greenpepper.server.domain.component.ContentType} object.
     * @return a boolean.
     */
    public static boolean containsSpecifications(ContentType type)
    {
        return containsSpecifications(type.getContentType());
    }

    /**
     * <p>containsSpecifications.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean containsSpecifications(String type)
    {
        return (type.equals(TEST.toString()) || type.equals(BOTH.toString()));
	}
}
