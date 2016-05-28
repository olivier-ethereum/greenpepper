package com.greenpepper.extensions.ognl;

import com.greenpepper.document.Document;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

/**
 * <p>OgnlSupport class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class OgnlSupport implements SystemUnderDevelopment
{
    static
    {
        ShouldBe.register( OgnlExpectation.class );
    }

    private final SystemUnderDevelopment systemUnderDevelopment;

    /**
     * <p>Constructor for OgnlSupport.</p>
     */
    public OgnlSupport()
    {
        this( new DefaultSystemUnderDevelopment() );
    }

    /**
     * <p>Constructor for OgnlSupport.</p>
     *
     * @param systemUnderDevelopment a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     */
    public OgnlSupport( SystemUnderDevelopment systemUnderDevelopment )
    {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    /**
     * <p>getFixture.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param params a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.reflect.Fixture} object.
     * @throws java.lang.Throwable if any.
     */
    public Fixture getFixture( String name, String... params ) throws Throwable
    {
        return new OgnlFixture( systemUnderDevelopment.getFixture( name, params ) );
    }

    /** {@inheritDoc} */
    public void addImport( String packageName )
    {
        systemUnderDevelopment.addImport(packageName);
    }

    /** {@inheritDoc} */
    public void onEndDocument(Document document)
    {
         
    }

    /** {@inheritDoc} */
    public void onStartDocument(Document document)
    {
        
    }
}
