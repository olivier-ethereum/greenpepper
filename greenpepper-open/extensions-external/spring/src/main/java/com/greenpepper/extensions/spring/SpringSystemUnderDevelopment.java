package com.greenpepper.extensions.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;

/**
 * <p>SpringSystemUnderDevelopment class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SpringSystemUnderDevelopment extends DefaultSystemUnderDevelopment
{
    private BeanFactory beanFactory;

    /**
     * <p>Constructor for SpringSystemUnderDevelopment.</p>
     *
     * @param applicationCtxes a {@link java.lang.String} object.
     */
    public SpringSystemUnderDevelopment(String... applicationCtxes)
    {
        // TODO move this to the onDocumentStart method . It's not a good idea to play with spring in the construtor
        this.beanFactory = new GreenPepperXMLAplicationContext(applicationCtxes).getBeanFactory();
    }
	
    /**
     * <p>Constructor for SpringSystemUnderDevelopment.</p>
     *
     * @param beanFactory a {@link org.springframework.beans.factory.BeanFactory} object.
     */
    public SpringSystemUnderDevelopment(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    /** {@inheritDoc} */
    @Override
    public Fixture getFixture(String fixtureName, String... params) throws Throwable
    {
		Fixture fixture;
		if (params.length != 0)
        {
		    // When params are used, we use the PlainOldSystemUnderDevelopment to instantiate the fixture
			// these params are passed to the constructor
            fixture = super.getFixture(fixtureName, params);
            // TODO : We still probably can inject members
        }
		else
		{
	        try
	        {
	            fixture = new DefaultFixture(beanFactory.getBean(fixtureName));
	        }
	        catch (NoSuchBeanDefinitionException e)
	        {
	            fixture = new DefaultFixture(instantiateAsAutowiredBean(fixtureName));
	        }
		}
		
		return fixture;
    }

    private Object instantiateAsAutowiredBean(String fixtureName) throws Exception
    {
        Class fixtureClass = loadType(fixtureName).getUnderlyingClass();

        BeanDefinition beanDef = new RootBeanDefinition(fixtureClass, RootBeanDefinition.AUTOWIRE_AUTODETECT);
        
        DefaultListableBeanFactory fallFactory = new DefaultListableBeanFactory(beanFactory);
        fallFactory.registerBeanDefinition(fixtureName, beanDef);

        return fallFactory.getBean(fixtureName);
    }
}
