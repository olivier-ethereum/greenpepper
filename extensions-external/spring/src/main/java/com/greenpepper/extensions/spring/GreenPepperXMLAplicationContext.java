package com.greenpepper.extensions.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * <p>GreenPepperXMLAplicationContext class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperXMLAplicationContext extends AbstractXmlApplicationContext
{
    private String[] configLocations;

    /**
     * Create a new ClassPathXmlApplicationContext, loading the definitions from
     * the given XML file and automatically refreshing the context.
     *
     * @param configLocation file path
     * @throws org.springframework.beans.BeansException if any.
     */
    public GreenPepperXMLAplicationContext(String configLocation) throws BeansException
    {
        this(new String[] { configLocation });
    }

    /**
     * Create a new ClassPathXmlApplicationContext, loading the definitions from
     * the given XML files and automatically refreshing the context.
     *
     * @param configLocations array of file paths
     * @throws org.springframework.beans.BeansException if any.
     */
    public GreenPepperXMLAplicationContext(String[] configLocations) throws BeansException
    {
        this(configLocations, true);
    }

    /**
     * Create a new ClassPathXmlApplicationContext, loading the definitions from
     * the given XML files.
     *
     * @param configLocations array of file paths
     * @param refresh
     *            whether to automatically refresh the context, loading all bean
     *            definitions and creating all singletons. Alternatively, call
     *            refresh manually after further configuring the context.
     * @see #refresh()
     * @throws org.springframework.beans.BeansException if any.
     */
    public GreenPepperXMLAplicationContext(String[] configLocations, boolean refresh) throws BeansException
    {
        this.configLocations = configLocations;
        if (refresh)
        {
            refresh();
        }
    }

    /**
     * Create a new ClassPathXmlApplicationContext with the given parent,
     * loading the definitions from the given XML files and automatically
     * refreshing the context.
     *
     * @param configLocations array of file paths
     * @param parent the parent context
     * @throws org.springframework.beans.BeansException if any.
     */
    public GreenPepperXMLAplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException
    {
        this(configLocations, true, parent);
    }

    /**
     * Create a new ClassPathXmlApplicationContext with the given parent,
     * loading the definitions from the given XML files.
     *
     * @param configLocations array of file paths
     * @param refresh
     *            whether to automatically refresh the context, loading all bean
     *            definitions and creating all singletons. Alternatively, call
     *            refresh manually after further configuring the context.
     * @param parent the parent context
     * @see #refresh()
     * @throws org.springframework.beans.BeansException if any.
     */
    public GreenPepperXMLAplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
            throws BeansException
    {
        super(parent);
        this.configLocations = configLocations;
        if (refresh)
        {
            refresh();
        }
    }

    /**
     * <p>Getter for the field <code>configLocations</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    protected String[] getConfigLocations()
    {
        return this.configLocations;
    }

    /** {@inheritDoc} */
    protected Resource getResourceByPath(String path)
    {
        String classPathPrefix = org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;
        if(path.startsWith(classPathPrefix))
        {
            return super.getResourceByPath(path.substring(classPathPrefix.length()));
        }

        if (path != null && path.startsWith("/"))
        {
            path = path.substring(1);
        }

        return new FileSystemResource(path);
    }
}
