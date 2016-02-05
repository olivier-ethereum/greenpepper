package com.greenpepper.samples.application.phonebook;

import java.net.URL;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


/**
 * <p>HibernateDatabase class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HibernateDatabase
{
    private static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";
    private final AnnotationConfiguration cfg;

    /**
     * <p>Constructor for HibernateDatabase.</p>
     *
     * @param properties a {@link java.util.Properties} object.
     * @throws org.hibernate.HibernateException if any.
     */
    public HibernateDatabase(Properties properties) throws HibernateException
    {
        cfg = new AnnotationConfiguration();        
        cfg.setProperties(properties); 
        setAnnotadedClasses();
        loadConfig();
    }
    
    /**
     * <p>createDatabase.</p>
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void createDatabase() throws HibernateException
    {
        new SchemaExport(cfg).create(false, true);
    }

    /**
     * <p>dropDatabase.</p>
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void dropDatabase() throws HibernateException
    {
        new SchemaExport(cfg).drop(false, true);
    }
    
    /**
     * <p>getConfiguration.</p>
     *
     * @return a {@link org.hibernate.cfg.Configuration} object.
     */
    public Configuration getConfiguration()
    {
        return cfg;
    }    

    /**
     * <p>getSessionFactory.</p>
     *
     * @return a {@link org.hibernate.SessionFactory} object.
     * @throws org.hibernate.HibernateException if any.
     */
    public SessionFactory getSessionFactory() throws HibernateException
    {
        return cfg.buildSessionFactory();
    }
    
    private void setAnnotadedClasses()
    {
        cfg.addAnnotatedClass(Country.class)
        .addAnnotatedClass(State.class)
        .addAnnotatedClass(PhoneBook.class)
        .addAnnotatedClass(PhoneBookEntry.class);
    }
    
    private void loadConfig()
    {
        URL xmlConfig = HibernateDatabase.class.getClassLoader().getResource(HIBERNATE_CONFIG_FILE);
        if (xmlConfig != null) cfg.configure(xmlConfig);
    }
}
