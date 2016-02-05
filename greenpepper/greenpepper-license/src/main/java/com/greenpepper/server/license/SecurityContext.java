package com.greenpepper.server.license;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.greenpepper.server.domain.Repository;

/**
 * <p>SecurityContext class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SecurityContext
{
    private final static TimeSource DEFAULT_TIME_SOURCE = new TimeSource() { public Date now() { return new Date(); } };

    private Set<Repository> grantedApplications = new HashSet<Repository>();
    private TimeSource timeSource;

    /**
     * <p>isMaxReached.</p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     * @param licenseMaxUsers a int.
     * @return a boolean.
     */
    public boolean isMaxReached(Repository repository, int licenseMaxUsers)
    {
        Set<Repository> clonedApp = new HashSet<Repository>(grantedApplications);
        
        int counter = 0;
        clonedApp.remove(repository);
        for (Repository repo : clonedApp)
        {
            if (repo.getType().equals(repository.getType()) && !repo.getBaseUrl().equals(repository.getBaseUrl()))
            {
                counter += repo.getMaxUsers();
            }
        }
        
        return repository.getMaxUsers() + counter > licenseMaxUsers;
    }

    /**
     * <p>grantAccess.</p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     */
    public void grantAccess(Repository repository)
    {
        grantedApplications.add(repository);
    }

    /**
     * <p>denyAccess.</p>
     *
     * @param repository a {@link com.greenpepper.server.domain.Repository} object.
     */
    public void denyAccess(Repository repository)
    {
        grantedApplications.remove(repository);
    }

    /**
     * <p>Constructor for SecurityContext.</p>
     */
    public SecurityContext()
    {
        this(DEFAULT_TIME_SOURCE);
    }

    /**
     * <p>Constructor for SecurityContext.</p>
     *
     * @param timeSource a {@link com.greenpepper.server.license.TimeSource} object.
     */
    public SecurityContext(TimeSource timeSource)
    {
        this.timeSource = timeSource;
    }

    /**
     * <p>now.</p>
     *
     * @return a {@link java.util.Date} object.
     */
    public Date now()
    {
        return timeSource.now();
    }
}
