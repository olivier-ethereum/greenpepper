package com.greenpepper.util;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Period class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Period implements Serializable
{
    private final Date start;

    private final Date end;

    private Period(Date start, Date end)
    {
        this.start = start;
        this.end = end;
    }

    /**
     * <p>to.</p>
     *
     * @param end a {@link java.util.Date} object.
     * @return a {@link com.greenpepper.util.Period} object.
     */
    public static Period to(Date end)
    {
        return new Period(null, end);
    }

    /**
     * <p>from.</p>
     *
     * @param start a {@link java.util.Date} object.
     * @return a {@link com.greenpepper.util.Period} object.
     */
    public static Period from(Date start)
    {
        return new Period(start, null);
    }

    /**
     * <p>fromTo.</p>
     *
     * @param start a {@link java.util.Date} object.
     * @param end a {@link java.util.Date} object.
     * @return a {@link com.greenpepper.util.Period} object.
     */
    public static Period fromTo(Date start, Date end)
    {
        return new Period(start, end);
    }

    /**
     * <p>includes.</p>
     *
     * @param date a {@link java.util.Date} object.
     * @return a boolean.
     */
    public boolean includes(Date date)
    {
        return (start == null || !start.after(date)) && 
               (end == null   || date.before(end));
    }

    /**
     * <p>beforeEnd.</p>
     *
     * @param date a {@link java.util.Date} object.
     * @return a boolean.
     */
    public boolean beforeEnd(Date date)
    {
        return (end == null   || date.before(end));
    }
    
    /**
     * <p>daysCount.</p>
     *
     * @return a int.
     */
    public int daysCount()
    {
    	if(start == null || end == null) return -1;
    	return (int)((end.getTime() - start.getTime()) / 86400000);
    }
}
