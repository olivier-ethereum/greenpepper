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

public class DefaultLogbackConfigurator implements Configurator {

    private Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

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

    @Override
    public void addStatus(Status status) {
        StatusManager sm = context.getStatusManager();
        sm.add(status);
    }

    @Override
    public void addInfo(String msg) {
        context.getStatusManager().add(new InfoStatus(msg, context));
    }

    @Override
    public void addInfo(String msg, Throwable ex) {
        context.getStatusManager().add(new InfoStatus(msg, context, ex));
    }

    @Override
    public void addWarn(String msg) {
        context.getStatusManager().add(new WarnStatus(msg, context));
    }

    @Override
    public void addWarn(String msg, Throwable ex) {
        context.getStatusManager().add(new WarnStatus(msg, context, ex));
    }

    @Override
    public void addError(String msg) {
        context.getStatusManager().add(new ErrorStatus(msg, context));
    }

    @Override
    public void addError(String msg, Throwable ex) {
        context.getStatusManager().add(new ErrorStatus(msg, context, ex));
    }

}
