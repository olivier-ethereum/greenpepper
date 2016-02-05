package com.greenpepper.server.configuration;

import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <p>ServerConfiguration class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ServerConfiguration
{
    private static String PROPERTIES_TAG = "properties";
    private static String PROPERTY_TAG = "property";
    private static String NAME_TAG = "name";
    
    private static ServerConfiguration config;
    
    private Document configDocument;
    private Properties properties = new DefaultServerProperties();

    private ServerConfiguration(){} 
        
    /**
     * <p>load.</p>
     *
     * @param url a {@link java.net.URL} object.
     * @return a {@link com.greenpepper.server.configuration.ServerConfiguration} object.
     * @throws java.lang.Exception if any.
     */
    public static ServerConfiguration load(URL url) throws Exception
    {
        config = new ServerConfiguration();
        config.loadConfig(url);
        return config;
    }
    
    /**
     * <p>instance.</p>
     *
     * @return a {@link com.greenpepper.server.configuration.ServerConfiguration} object.
     */
    public static ServerConfiguration instance()
    {
        if(config == null) throw new IllegalStateException("Config not loaded");
        return config;
    }

    /**
     * <p>Getter for the field <code>properties</code>.</p>
     *
     * @return a {@link java.util.Properties} object.
     */
    public Properties getProperties()
    {
        return properties;
    }
    
    private void loadConfig(URL url) throws DocumentException
    {
        SAXReader reader = new SAXReader();
        configDocument = reader.read(url);
        loadProperties();
    }
    
    private void loadProperties()
    {
        Iterator iter = configDocument.getRootElement().elementIterator(PROPERTIES_TAG);
        while(iter.hasNext())
        {
            Element elementProperties = (Element)iter.next();
            for (Iterator iterProperty = elementProperties.elementIterator(PROPERTY_TAG); iterProperty.hasNext();)
            {
                Element elementProperty = (Element)iterProperty.next();
                Attribute attributeName = elementProperty.attribute(NAME_TAG);
                properties.put(getStringData(attributeName), getStringData(elementProperty));
            }
        }
    }
    
    private String getStringData(Element element)
    {
        String value = element.getStringValue();
        value = value.trim().replace('\\', '/');
        return value;
    }
    
    private String getStringData(Attribute element)
    {
        String value = element.getStringValue();
        value = value.trim().replace('\\', '/');
        return value;
    }
}
