package com.greenpepper.confluence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class GreenPepperServerConfigurationTest {

    @Test
    public void testToString() throws Exception {
        GreenPepperServerConfiguration configuration = new GreenPepperServerConfiguration(); 
        String output = "{\"properties\":{\"licence.cipher.key\":\"gr33np3pp3r\",\"hibernate.cache.provider_class\":\"org.hibernate.cache.NoCacheProvider\","
                + "\"licence.keystore\":\"publicCerts.store\",\"hibernate.setup\":\"true\",\"licence.subject\":\"GreenPepper\","
                + "\"licence.key.alias\":\"publiccert\",\"hibernate.hbm2ddl.auto\":\"update\",\"hibernate.connection.datasource\":\"java:comp/env/jdbc/GreenPepperDS\","
                + "\"hibernate.show_sql\":\"false\",\"jta.UserTransaction\":\"java:comp/env/UserTransaction\",\"licence.keystore.pwd\":\"gr33np3pp3r\"},\"setupComplete\":false}";
        assertEquals(output,configuration.toString());
    }

    @Test
    public void testFromString() throws Exception {
        String input = "{\"properties\":{\"licence.cipher.key\":\"gr33np3pp3r\",\"hibernate.cache.provider_class\":\"org.hibernate.cache.NoCacheProvider\",\"licence.keystore\":\"publicCerts.store\",\"hibernate.setup\":\"true\",\"licence.subject\":\"GreenPepper Revival Test\",\"licence.key.alias\":\"publiccert\",\"hibernate.hbm2ddl.auto\":\"update\",\"hibernate.connection.datasource\":\"java:comp/env/jdbc/GreenPepperDS\",\"hibernate.show_sql\":\"false\",\"jta.UserTransaction\":\"java:comp/env/UserTransaction\",\"licence.keystore.pwd\":\"gr33np3pp3r\"},\"setupComplete\":true}";
        GreenPepperServerConfiguration config = GreenPepperServerConfiguration.fromString(input);
        assertTrue(config.isSetupComplete());
        assertEquals("GreenPepper Revival Test",config.getProperties().getProperty("licence.subject"));
    }

}
