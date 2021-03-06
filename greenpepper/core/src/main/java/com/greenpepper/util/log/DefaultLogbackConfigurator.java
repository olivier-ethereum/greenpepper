package com.greenpepper.util.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import com.greenpepper.GreenPepper;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.reflect.DefaultFixture;

public class DefaultLogbackConfigurator implements Configurator {

    private Context context;

    /** {@inheritDoc} */
    @Override
    public void setContext(Context context) {
        this.context = context;

    }

    /** {@inheritDoc} */
    @Override
    public Context getContext() {
        return context;
    }

    /** {@inheritDoc} */
    @Override
    public void addStatus(Status status) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void addInfo(String msg) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void addInfo(String msg, Throwable ex) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void addWarn(String msg) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void addWarn(String msg, Throwable ex) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void addError(String msg) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void addError(String msg, Throwable ex) {
        // I don't care
    }

    /** {@inheritDoc} */
    @Override
    public void configure(LoggerContext loggerContext) {
        StatusManager sm = loggerContext.getStatusManager();
        if (sm != null) {
            sm.add(new InfoStatus("Setting up default configuration.", loggerContext));
        }
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(loggerContext);
        ca.setName("console");
        PatternLayoutEncoder pl = new PatternLayoutEncoder();
        pl.setContext(loggerContext);
        pl.setPattern("%d{HH:mm:ss.SSS} [GreenPepper] %msg%n");
        pl.start();

        ca.setEncoder(pl);
        ca.start();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(ca);
        rootLogger.setLevel(Level.WARN);
        loggerContext.getLogger("com.greenpepper").setLevel(Level.INFO);
        if (GreenPepper.isDebugEnabled()) {
            loggerContext.getLogger(Annotations.GREENPEPPER_EXCEPTIONS_LOGGERNAME).setLevel(Level.INFO);
            loggerContext.getLogger(DefaultFixture.GREENPEPPER_FIXTURES_LOGGERNAME).setLevel(Level.INFO);
        }
    }

}