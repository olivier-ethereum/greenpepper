package com.greenpepper.util.log;

import ch.qos.logback.classic.util.ContextInitializer;
import com.greenpepper.GreenPepper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreenPepperLogger {

    private static final String GREENPEPPER_DEBUG_SYSPROP = "greenpepper.debug";
    private static boolean initialized = false;

    public static Logger getLogger(String name) {

        Logger askedLogger;
        if (!initialized) {
            synchronized (GreenPepperLogger.class) {
                String originalLogbackConfigFileValue = System.getProperty(ContextInitializer.CONFIG_FILE_PROPERTY);
                System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "greenpepper-logback.xml");
                System.setProperty(GREENPEPPER_DEBUG_SYSPROP, Boolean.toString(GreenPepper.isDebugEnabled()));
                askedLogger = LoggerFactory.getLogger(name);
                if (StringUtils.isNotBlank(originalLogbackConfigFileValue)) {
                    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, originalLogbackConfigFileValue);
                } else {
                    System.clearProperty(ContextInitializer.CONFIG_FILE_PROPERTY);
                }
                System.clearProperty(GREENPEPPER_DEBUG_SYSPROP);
                initialized = true;
            }
        }  else {
            askedLogger = LoggerFactory.getLogger(name);
        }
        return askedLogger;
    }

    public static Logger getLogger(Class className) {
        return getLogger(className.getName());
    }

}
