package com.greenpepper.server.domain.dao;

import com.greenpepper.server.domain.SystemInfo;

/**
 * <p>SystemInfoDao interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface SystemInfoDao
{
    /**
     * <p>getSystemInfo.</p>
     *
     * @return The SystemInfo
     */
    public SystemInfo getSystemInfo();
    
    /**
     * Stores the SystemInfo.
     * </p>
     *
     * @param systemInfo a {@link com.greenpepper.server.domain.SystemInfo} object.
     */
    public void store(SystemInfo systemInfo);
}
