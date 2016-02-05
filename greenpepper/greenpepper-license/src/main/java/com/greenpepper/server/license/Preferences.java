package com.greenpepper.server.license;

import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;

/**
 * <p>Preferences class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Preferences extends java.util.prefs.Preferences
{
	private static Preferences preferences;
	private byte[] lic;
	
	/**
	 * <p>instance.</p>
	 *
	 * @return a {@link com.greenpepper.server.license.Preferences} object.
	 */
	public static Preferences instance()
	{
		if(preferences == null)
			preferences = new Preferences();
		
		return preferences;
	}
	
    /**
     * <p>getByteArray.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param def an array of byte.
     * @return an array of byte.
     */
    public byte[] getByteArray(String key, byte[] def)
    {
        return lic;
    }
    
    /**
     * <p>putByteArray.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param lic an array of byte.
     */
    @SuppressWarnings("unchecked")
    public void putByteArray(String key, byte[] lic)
    {
        this.lic = lic;
    }
    
    /** {@inheritDoc} */
    public void remove(String key)
    {
    	this.lic = null;
    }
    
    /**
     * <p>isUserNode.</p>
     *
     * @return a boolean.
     */
    public boolean isUserNode()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String absolutePath(){ /* TODO Auto-generated method stub */ return null;}

    /** {@inheritDoc} */
    @Override
    public void addNodeChangeListener(NodeChangeListener arg0){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void addPreferenceChangeListener(PreferenceChangeListener arg0){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public String[] childrenNames() throws BackingStoreException{ /* TODO Auto-generated method stub */ return null;}
    
    /** {@inheritDoc} */
    @Override
    public void clear() throws BackingStoreException{ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void exportNode(OutputStream arg0) throws IOException, BackingStoreException{ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void exportSubtree(OutputStream arg0) throws IOException, BackingStoreException{ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void flush() throws BackingStoreException{ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public String get(String arg0, String arg1){ /* TODO Auto-generated method stub */ return null;}    

    /** {@inheritDoc} */
    @Override
    public boolean getBoolean(String arg0, boolean arg1){ /* TODO Auto-generated method stub */ return false;}

    /** {@inheritDoc} */
    @Override
    public double getDouble(String arg0, double arg1){ /* TODO Auto-generated method stub */ return 0;}

    /** {@inheritDoc} */
    @Override
    public float getFloat(String arg0, float arg1){ /* TODO Auto-generated method stub */ return 0;}

    /** {@inheritDoc} */
    @Override
    public int getInt(String arg0, int arg1){ /* TODO Auto-generated method stub */ return 0;}

    /** {@inheritDoc} */
    @Override
    public long getLong(String arg0, long arg1){ /* TODO Auto-generated method stub */ return 0;}

    /** {@inheritDoc} */
    @Override
    public String[] keys() throws BackingStoreException{ /* TODO Auto-generated method stub */ return null;}

    /** {@inheritDoc} */
    @Override
    public String name(){ /* TODO Auto-generated method stub */ return null;}

    /** {@inheritDoc} */
    @Override
    public java.util.prefs.Preferences node(String arg0){ /* TODO Auto-generated method stub */ return null;}

    /** {@inheritDoc} */
    @Override
    public boolean nodeExists(String arg0) throws BackingStoreException{ /* TODO Auto-generated method stub */ return false;}

    /** {@inheritDoc} */
    @Override
    public java.util.prefs.Preferences parent(){ /* TODO Auto-generated method stub */ return null;}

    /** {@inheritDoc} */
    @Override
    public void put(String arg0, String arg1){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void putBoolean(String arg0, boolean arg1){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void putDouble(String arg0, double arg1){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void putFloat(String arg0, float arg1){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void putInt(String arg0, int arg1){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void putLong(String arg0, long arg1){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void removeNode() throws BackingStoreException{ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void removeNodeChangeListener(NodeChangeListener arg0){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void removePreferenceChangeListener(PreferenceChangeListener arg0){ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public void sync() throws BackingStoreException{ /* TODO Auto-generated method stub */ }

    /** {@inheritDoc} */
    @Override
    public String toString(){ /* TODO Auto-generated method stub */ return null;}
}
