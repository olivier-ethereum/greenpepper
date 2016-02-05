package com.greenpepper.server.database.hibernate;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.greenpepper.server.database.SessionService;

/**
 * <p>HibernateSessionService class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class HibernateSessionService implements SessionService
{
    private final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();
    private final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
    private SessionFactory sessionFactory;

	/**
	 * <p>Constructor for HibernateSessionService.</p>
	 *
	 * @param properties a {@link java.util.Properties} object.
	 * @throws org.hibernate.HibernateException if any.
	 */
	public HibernateSessionService(Properties properties) throws HibernateException
    {
        HibernateDatabase db = new HibernateDatabase(properties);
        sessionFactory = db.getSessionFactory();
    }

	/**
	 * <p>Constructor for HibernateSessionService.</p>
	 */
	public HibernateSessionService()
	{

	}

	/**
	 * <p>Setter for the field <code>sessionFactory</code>.</p>
	 *
	 * @param sessionFactory a {@link org.hibernate.SessionFactory} object.
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	/**
	 * <p>startSession.</p>
	 *
	 * @throws org.hibernate.HibernateException if any.
	 */
	public void startSession() throws HibernateException
    {
        initSession();
    }

    /**
     * <p>getSession.</p>
     *
     * @return a {@link org.hibernate.Session} object.
     * @throws org.hibernate.HibernateException if any.
     */
    public Session getSession() throws HibernateException
    {
        Session s = threadSession.get();
        if (s == null)
        {
            s = initSession();
        }

        return s;
    }

    /**
     * closeSession
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void closeSession() throws HibernateException
    {
        Session s = threadSession.get();
        threadSession.set(null);
        if (s != null)
        {
            s.close();
        }
        
        Transaction tx = threadTransaction.get();
        if (tx != null)
        {
            threadTransaction.set(null);
            //log warning todo
        }
    }

    /**
     * <p>beginTransaction.</p>
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void beginTransaction() throws HibernateException
    {
        Transaction tx = threadTransaction.get();
        if (tx == null)
        {
            tx = getSession().beginTransaction();
            threadTransaction.set(tx);
        }
    }

    /**
     * <p>commitTransaction.</p>
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void commitTransaction() throws HibernateException
    {
        Transaction tx = threadTransaction.get();
        threadTransaction.set(null);
        if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
        {
            tx.commit();
        }
    }

    /**
     * <p>rollbackTransaction.</p>
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void rollbackTransaction() throws HibernateException
    {
        Transaction tx = threadTransaction.get();
        try
        {
            threadTransaction.set(null);
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
            {
                tx.rollback();
            }
        }
        finally
        {
            closeSession();
        }
    }

    /**
     * <p>close.</p>
     *
     * @throws org.hibernate.HibernateException if any.
     */
    public void close() throws HibernateException
    {
        sessionFactory.close();
    }

    private synchronized Session initSession()
    {
        Session s = sessionFactory.openSession();
        threadSession.set(s);
        return s;
    }
}
