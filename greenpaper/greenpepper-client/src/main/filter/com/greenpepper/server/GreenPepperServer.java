package com.greenpepper.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class GreenPepperServer
{
    private static Date versionDate;

    public static final String VERSION = "${pom.version}";

    public static Date versionDate()
    {
        String date = "${timestamp}";
        try {
            versionDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to get version date", e);
        }
        return versionDate;
    }
}
