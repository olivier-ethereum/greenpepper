package com.greenpepper.confluence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfluencePlugin {
    public static final String VERSION = "${pom.version}";

    private static Date versionDate;

    public static final Date versionDate() {
        String date = "${timestamp}";
        try {
            versionDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to get version date", e);
        }
        return versionDate;
    }
}
