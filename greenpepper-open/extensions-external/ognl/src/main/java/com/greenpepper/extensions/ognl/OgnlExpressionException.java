package com.greenpepper.extensions.ognl;

/**
 * <p>Abstract OgnlExpressionException class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class OgnlExpressionException extends RuntimeException
{
    private final String expression;

    /**
     * <p>Constructor for OgnlExpressionException.</p>
     *
     * @param expression a {@link java.lang.String} object.
     */
    public OgnlExpressionException( String expression )
    {
        this.expression = expression;
    }

    /**
     * <p>Getter for the field <code>expression</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getExpression()
    {
        return expression;
    }
}
