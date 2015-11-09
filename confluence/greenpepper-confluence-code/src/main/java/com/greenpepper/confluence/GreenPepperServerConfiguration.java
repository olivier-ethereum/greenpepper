/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.confluence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.greenpepper.server.configuration.DefaultServerProperties;

public class GreenPepperServerConfiguration implements Serializable {
    
    private static final MappingJsonFactory jsonF = new MappingJsonFactory();

    private static final long serialVersionUID = 1L;

    private boolean isSetupComplete;
	private Properties properties = new DefaultServerProperties();

    public boolean isSetupComplete()
	{
		return isSetupComplete;
	}

    public void setSetupComplete(boolean setupComplete)
	{
		isSetupComplete = setupComplete;
	}

    public Properties getProperties()
	{
		return properties;
	}

    public void setProperties(Properties properties)
	{
		this.properties = properties;
	}

    /**
     * We use this method to store the Configuration as JSON
     */
	public String toString()
	{
	    try {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator createGenerator = jsonF.createGenerator(out);
            createGenerator.writeObject(this);
            createGenerator.flush();
            createGenerator.close();
            String output = new String(out.toByteArray());
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert this instance to a JSON using Jackson, fallbacking to manual", e);
        }
	}
	
	public static GreenPepperServerConfiguration fromString(String input) throws JsonParseException, IOException {
	    JsonParser parser = jsonF.createParser(input);
	    TypeReference<GreenPepperServerConfiguration> ref = new TypeReference<GreenPepperServerConfiguration>() { };
	    return parser.readValueAs(ref);
	}
}