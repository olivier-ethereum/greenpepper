package com.greenpepper.server;

/**
 * ServerPropertiesManager.
 * For server properties persistence.
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 * @version $Id: $Id
 */
public interface ServerPropertiesManager
{
    /** Constant <code>URL="GREENPEPPER_URL"</code> */
    public final static String URL = "GREENPEPPER_URL";
    /** Constant <code>HANDLER="GREENPEPPER_HANDLER"</code> */
    public final static String HANDLER = "GREENPEPPER_HANDLER";
    /** Constant <code>PROJECT="GREENPEPPER_PROJECT"</code> */
    public final static String PROJECT = "GREENPEPPER_PROJECT";
    /** Constant <code>SEQUENCE="greenpepper."</code> */
    public final static String SEQUENCE = "greenpepper.";
    
    /**
     * Retrieves the property for the specified key and identifier.
     * </p>
     *
     * @param key a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     * @return the property for the specified key and params.
     */
    public String getProperty(String key, String identifier);
    
    /**
     * Saves the property value for the specified key and identifier.
     * </p>
     *
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @param identifier a {@link java.lang.String} object.
     */
    public void setProperty(String key, String value, String identifier);
}
