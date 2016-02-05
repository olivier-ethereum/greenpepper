package com.greenpepper.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>FormatedDate class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class FormatedDate
{
    private Timestamp date;
    
    /**
     * <p>Constructor for FormatedDate.</p>
     *
     * @param date a {@link java.sql.Timestamp} object.
     */
    public FormatedDate(Timestamp date)
    {
        this.date = date;
    }
    
    /**
     * <p>Constructor for FormatedDate.</p>
     *
     * @param date a {@link java.util.Date} object.
     */
    public FormatedDate(Date date)
    {
        if(date != null) this.date = new Timestamp(date.getTime());
    }
    
    /**
     * <p>Constructor for FormatedDate.</p>
     *
     * @param formatedDate a {@link java.lang.String} object.
     */
    public FormatedDate(String formatedDate)
    {
        if(!StringUtil.isEmpty(formatedDate))
        {
            GregorianCalendar g = new GregorianCalendar();
            g.set(GregorianCalendar.YEAR, new Integer(formatedDate.substring(0, 4)));
            g.set(GregorianCalendar.MONTH, new Integer(formatedDate.substring(5, 7)) - 1);
            g.set(GregorianCalendar.DAY_OF_MONTH, new Integer(formatedDate.substring(8, 10)));
            if(formatedDate.length() > 10)
            {
                g.set(GregorianCalendar.HOUR, new Integer(formatedDate.substring(11, 13)));
                g.set(GregorianCalendar.MINUTE, new Integer(formatedDate.substring(14, 16)));
                g.set(GregorianCalendar.SECOND, new Integer(formatedDate.substring(17, 19)));
            }
            
            date = new Timestamp(g.getTime().getTime());
        }
    }
    
    /**
     * <p>getFormatedTimestamp.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFormatedTimestamp()
    {
        if(date == null){return "";}
        
        StringBuilder sb = new StringBuilder();
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(date);
        sb.append(String.valueOf(g.get(GregorianCalendar.YEAR))).append("-");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.MONTH) + 1)).append("-");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.DAY_OF_MONTH))).append(" ");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.HOUR))).append(":");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.MINUTE))).append(":");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.SECOND)));
        
        return sb.toString();
    }
    
    /**
     * <p>getFormatedDate.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFormatedDate()
    {
        if(date == null){return "";}
        
        StringBuilder sb = new StringBuilder();
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(date);
        sb.append(String.valueOf(g.get(GregorianCalendar.YEAR))).append("-");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.MONTH) + 1)).append("-");
        sb.append(twoDigitFormat(g.get(GregorianCalendar.DAY_OF_MONTH)));
        
        return sb.toString();
    }  
    
    /**
     * <p>asDate.</p>
     *
     * @return a {@link java.util.Date} object.
     */
    public Date asDate()
    {        
        if(date == null){return null;}
        return new Date(date.getTime());
    } 
    
    /**
     * <p>asTimestamp.</p>
     *
     * @return a {@link java.sql.Timestamp} object.
     */
    public Timestamp asTimestamp()
    {
        return date;
    } 
    
    private String twoDigitFormat(int num)
    {
        return num > 9 ? String.valueOf(num) : "0" + num;
    }
}
