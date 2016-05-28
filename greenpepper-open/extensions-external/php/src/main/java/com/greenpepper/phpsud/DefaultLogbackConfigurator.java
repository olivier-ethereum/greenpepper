package com.greenpepper.phpsud;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;

/**
 * <p>DefaultLogbackConfigurator class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
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
    public void configure(LoggerContext loggerContext) {
        StatusManager sm = loggerContext.getStatusManager();
        if (sm != null) {
            sm.add(new InfoStatus("Setting up GreenPepper PHP Runner configuration.", loggerContext));
        }
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(loggerContext);
        ca.setName("console");
        PatternLayoutEncoder pl = new PatternLayoutEncoder();
        pl.setContext(loggerContext);
        pl.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS}  %-5level   %-36logger{36} - %msg%n");
        pl.start();

        ca.setEncoder(pl);
        ca.start();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(ca);
        rootLogger.setLevel(Level.INFO);
    }

    /** {@inheritDoc} */
    @Override
    public void addStatus(Status status) {
        StatusManager sm = context.getStatusManager();
        sm.add(status);
    }

    /** {@inheritDoc} */
    @Override
    public void addInfo(String msg) {
        context.getStatusManager().add(new InfoStatus(msg, context));
    }

    /** {@inheritDoc} */
    @Override
    public void addInfo(String msg, Throwable ex) {
        context.getStatusManager().add(new InfoStatus(msg, context, ex));
    }

    /** {@inheritDoc} */
    @Override
    public void addWarn(String msg) {
        context.getStatusManager().add(new WarnStatus(msg, context));
    }

    /** {@inheritDoc} */
    @Override
    public void addWarn(String msg, Throwable ex) {
        context.getStatusManager().add(new WarnStatus(msg, context, ex));
    }

    /** {@inheritDoc} */
    @Override
    public void addError(String msg) {
        context.getStatusManager().add(new ErrorStatus(msg, context));
    }

    /** {@inheritDoc} */
    @Override
    public void addError(String msg, Throwable ex) {
        context.getStatusManager().add(new ErrorStatus(msg, context, ex));
    }

}
