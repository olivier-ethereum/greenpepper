package com.greenpepper.server;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public final class GreenPepperServer
{

    private static Date versionDate;

    public static String VERSION;
    static {
        try {
            Properties info = new Properties();
            InputStream serverInfo = GreenPepperServer.class.getResourceAsStream("/com/greenpepper/server-info.properties");
            info.load(serverInfo);
            VERSION = info.getProperty("server.version");
            String date = info.getProperty("server.version.date");
            versionDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load GreenPepperServer.class", e);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load GreenPepperServer.class", e);
        }

    }

    public static Date versionDate()
    {
        return versionDate;
    }
}
