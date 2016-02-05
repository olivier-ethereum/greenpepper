package com.greenpepper.server.database.hibernate;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.RepositoryTypeClass;
import com.greenpepper.server.domain.Requirement;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemInfo;
import com.greenpepper.server.domain.SystemUnderTest;

/**
 * <p>HibernateDatabase class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HibernateDatabase
{
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
        cfg.addAnnotatedClass(SystemInfo.class)
        .addAnnotatedClass(Project.class)
        .addAnnotatedClass(Runner.class)
        .addAnnotatedClass(EnvironmentType.class)
        .addAnnotatedClass(Repository.class)
        .addAnnotatedClass(RepositoryType.class)
        .addAnnotatedClass(RepositoryTypeClass.class)
        .addAnnotatedClass(SystemUnderTest.class)
        .addAnnotatedClass(Requirement.class)
        .addAnnotatedClass(Specification.class)
        .addAnnotatedClass(Reference.class)
        .addAnnotatedClass(Execution.class);
    }

}
