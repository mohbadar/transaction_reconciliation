package com.tutuka.lib.logger;

import java.util.HashMap;
import java.util.Map;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;
import com.tutuka.lib.lang.exception.common.IllegalArgumentException;
import com.tutuka.lib.lang.exception.common.IllegalStateException;
import com.tutuka.lib.logger.appender.ConsoleAppender;
import com.tutuka.lib.logger.appender.FileAppender;
import com.tutuka.lib.logger.appender.RollingFileAppender;
import com.tutuka.lib.logger.util.ConfigurationDefault;
import com.tutuka.lib.logger.util.LogExeptionCodeConstant;
import com.tutuka.lib.logger.util.LogLevel;
import com.tutuka.lib.logger.exception.ClassNameNotFoundException;
import com.tutuka.lib.logger.exception.EmptyPatternException;
import com.tutuka.lib.logger.exception.FileNameNotProvided;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
/**
 * Logback implementation class for
 *
 */
public class LoggerImpl implements Logger {

    private static final Map<String, Appender<ILoggingEvent>> rollingFileAppenders = new HashMap<>();
    private static final Map<String, Appender<ILoggingEvent>> fileAppenders = new HashMap<>();
    /**
     * Logger Instance per Class
     */
    private final ch.qos.logback.classic.Logger logger;

    /**
     * Display pattern of logs
     */
    private static final String LOGDISPLAY = "{} - {} - {} - {}";

    /**
     * Builds a logger instance
     *
     * @param ConsoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param name                 name of calling class to get logger
     */
    private LoggerImpl(ConsoleAppender ConsoleAppender, String name, LogLevel logLevel) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.logger = context.getLogger(name);
        PatternLayoutEncoder ple = getdefaultPattern(context);
        ch.qos.logback.core.ConsoleAppender<ILoggingEvent> consoleAppender = new ch.qos.logback.core.ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(ple);
        consoleAppender.setName(ConsoleAppender.getAppenderName());
        consoleAppender.setImmediateFlush(ConsoleAppender.isImmediateFlush());
        consoleAppender.setTarget(ConsoleAppender.getTarget());
        consoleAppender.start();
        if (logLevel != null) {
            this.logger.setLevel(Level.valueOf(logLevel.getLevel()));
        } else {
            this.logger.setLevel(Level.valueOf(LogLevel.DEBUG.getLevel()));
        }
        this.logger.setAdditive(false);
        this.logger.addAppender(consoleAppender);
    }

    /**
     * Builds a logger instance
     *
     * @param FileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param name              name of calling class to get logger
     */
    private LoggerImpl(FileAppender FileAppender, String name, LogLevel logLevel) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.logger = context.getLogger(name);
        this.logger.setAdditive(false);
        PatternLayoutEncoder ple = getdefaultPattern(context);
        ch.qos.logback.core.FileAppender<ILoggingEvent> fileAppender = null;
        if (!fileAppenders.containsKey(FileAppender.getAppenderName())) {
            fileAppender = new ch.qos.logback.core.FileAppender<>();
            fileAppender.setContext(context);
            fileAppender.setEncoder(ple);
            fileAppender.setName(FileAppender.getAppenderName());
            fileAppender.setImmediateFlush(FileAppender.isImmediateFlush());
            fileAppender.setAppend(FileAppender.isAppend());
            fileAppender.setFile(FileAppender.getFileName());
            fileAppender.setPrudent(FileAppender.isPrudent());
            fileAppender.start();
            fileAppenders.put(fileAppender.getName(), fileAppender);
        } else {
            fileAppender = (ch.qos.logback.core.FileAppender<ILoggingEvent>) fileAppenders
                    .get(FileAppender.getAppenderName());
        }
        if (logLevel != null) {
            this.logger.setLevel(Level.valueOf(logLevel.getLevel()));
        } else {
            this.logger.setLevel(Level.valueOf(LogLevel.DEBUG.getLevel()));
        }
        this.logger.addAppender(fileAppender);
    }

    /**
     * Builds a logger instance
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param name                     name of calling class to get logger
     */
    private LoggerImpl(RollingFileAppender RollingFileAppender, String name, LogLevel logLevel) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.logger = context.getLogger(name);
        this.logger.setAdditive(false);
        PatternLayoutEncoder ple = getdefaultPattern(context);
        ch.qos.logback.core.rolling.RollingFileAppender<ILoggingEvent> rollingFileAppender = null;
        if (!rollingFileAppenders.containsKey(RollingFileAppender.getAppenderName())) {
            rollingFileAppender = new ch.qos.logback.core.rolling.RollingFileAppender<>();
            rollingFileAppender.setContext(context);
            rollingFileAppender.setEncoder(ple);
            rollingFileAppender.setName(RollingFileAppender.getAppenderName());
            rollingFileAppender.setImmediateFlush(RollingFileAppender.isImmediateFlush());
            rollingFileAppender.setFile(RollingFileAppender.getFileName());
            rollingFileAppender.setAppend(RollingFileAppender.isAppend());
            rollingFileAppender.setPrudent(RollingFileAppender.isPrudent());
            if (RollingFileAppender.getMaxFileSize().trim().isEmpty()) {
                configureTimeBasedRollingPolicy(RollingFileAppender, context, rollingFileAppender);
            } else {
                configureSizeAndTimeBasedPolicy(RollingFileAppender, context, rollingFileAppender);
            }
            rollingFileAppenders.put(rollingFileAppender.getName(), rollingFileAppender);
            rollingFileAppender.start();
        } else {
            rollingFileAppender = (ch.qos.logback.core.rolling.RollingFileAppender<ILoggingEvent>) rollingFileAppenders
                    .get(RollingFileAppender.getAppenderName());
        }
        if (logLevel != null) {
            this.logger.setLevel(Level.valueOf(logLevel.getLevel()));
        } else {
            this.logger.setLevel(Level.valueOf(LogLevel.DEBUG.getLevel()));
        }
        this.logger.addAppender(rollingFileAppender);

    }

    /**
     * Configures size and time based policy
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance to get
     *                                 values'p
     * @param context                  context of logger
     * @param rollingFileAppender      {@link RollingFileAppender} instance by which
     *                                 this policy will attach
     */
    private void configureSizeAndTimeBasedPolicy(RollingFileAppender RollingFileAppender, LoggerContext context,
                                                 ch.qos.logback.core.rolling.RollingFileAppender<ILoggingEvent> rollingFileAppender) {
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> sizeAndTimeBasedRollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        sizeAndTimeBasedRollingPolicy.setContext(context);

        sizeAndTimeBasedRollingPolicy.setFileNamePattern(RollingFileAppender.getFileNamePattern());
        sizeAndTimeBasedRollingPolicy.setMaxHistory(RollingFileAppender.getMaxHistory());
        if (RollingFileAppender.getTotalCap() != null
                && !RollingFileAppender.getTotalCap().trim().isEmpty()) {
            sizeAndTimeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf(RollingFileAppender.getTotalCap()));
        }
        if (RollingFileAppender.getMaxFileSize() != null) {
            sizeAndTimeBasedRollingPolicy.setMaxFileSize(FileSize.valueOf(RollingFileAppender.getMaxFileSize()));
        }
        sizeAndTimeBasedRollingPolicy.setParent(rollingFileAppender);
        rollingFileAppender.setRollingPolicy(sizeAndTimeBasedRollingPolicy);
        sizeAndTimeBasedRollingPolicy.start();
    }

    /**
     * Configures time based policy
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance to get
     *                                 values
     * @param context                  context of logger
     * @param rollingFileAppender      {@link RollingFileAppender} instance by which
     *                                 this policy will attach
     */
    private void configureTimeBasedRollingPolicy(RollingFileAppender RollingFileAppender, LoggerContext context,
                                                 ch.qos.logback.core.rolling.RollingFileAppender<ILoggingEvent> rollingFileAppender) {
        TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy = new TimeBasedRollingPolicy<>();
        timeBasedRollingPolicy.setContext(context);
        timeBasedRollingPolicy.setFileNamePattern(RollingFileAppender.getFileNamePattern());
        timeBasedRollingPolicy.setMaxHistory(RollingFileAppender.getMaxHistory());
        if (RollingFileAppender.getFileNamePattern().contains("%i")) {
            throw new BaseUncheckedException(LogExeptionCodeConstant.PATTERNSYNTAXEXCEPTION.getValue(),
                    LogExeptionCodeConstant.PATTERNSYNTAXEXCEPTIONMESSAGENOTI.getValue());
        }
        if (RollingFileAppender.getTotalCap() != null
                && !RollingFileAppender.getTotalCap().trim().isEmpty()) {
            timeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf(RollingFileAppender.getTotalCap()));
        }
        timeBasedRollingPolicy.setParent(rollingFileAppender);
        rollingFileAppender.setRollingPolicy(timeBasedRollingPolicy);
        timeBasedRollingPolicy.start();
    }

    /**
     * Verifies configurations
     *
     * @param consoleAppender {@link ConsoleAppender} instance which contains all
     *                        configurations
     * @param name            name of the calling class
     * @param loglevel        log level
     * @return Configured {@link Logger} instance
     */
    public static Logger getConsoleLogger(ConsoleAppender consoleAppender, String name, LogLevel loglevel) {
        if (name.trim().isEmpty()) {
            throw new ClassNameNotFoundException(LogExeptionCodeConstant.CLASSNAMENOTFOUNDEXEPTION.getValue(),
                    LogExeptionCodeConstant.CLASSNAMENOTFOUNDEXEPTIONMESSAGE.getValue());
        } else {
            return new LoggerImpl(consoleAppender, name, loglevel);
        }
    }

    /**
     * Verifies configurations
     *
     * @param fileAppender {@link FileAppender} instance which contains all
     *                     configurations
     * @param name         name of the calling class
     * @param loglevel     log level
     * @return Configured {@link Logger} instance
     */
    public static Logger getFileLogger(FileAppender fileAppender, String name, LogLevel loglevel) {

        if (fileAppender.getFileName() == null)
            throw new FileNameNotProvided(LogExeptionCodeConstant.FILENAMENOTPROVIDED.getValue(),
                    LogExeptionCodeConstant.FILENAMENOTPROVIDEDMESSAGENULL.getValue());
        else if (fileAppender.getFileName().trim().isEmpty())
            throw new FileNameNotProvided(LogExeptionCodeConstant.FILENAMENOTPROVIDED.getValue(),
                    LogExeptionCodeConstant.FILENAMENOTPROVIDEDMESSAGEEMPTY.getValue());
        else if (name.trim().isEmpty())
            throw new ClassNameNotFoundException(LogExeptionCodeConstant.CLASSNAMENOTFOUNDEXEPTION.getValue(),
                    LogExeptionCodeConstant.CLASSNAMENOTFOUNDEXEPTIONMESSAGE.getValue());
        else {
            return new LoggerImpl(fileAppender, name, loglevel);
        }
    }

    /**
     * Verifies configurations
     *
     * @param rollingFileAppender {@link RollingFileAppender} instance which
     *                            contains all configurations
     * @param name                name of the calling class
     * @param loglevel            log level
     * @return Configured {@link Logger} instance
     */
    public static Logger getRollingFileLogger(RollingFileAppender rollingFileAppender, String name, LogLevel loglevel) {
        if (rollingFileAppender.getFileNamePattern() == null)
            throw new EmptyPatternException(LogExeptionCodeConstant.EMPTYPATTERNEXCEPTION.getValue(),
                    LogExeptionCodeConstant.EMPTYPATTERNEXCEPTIONMESSAGENULL.getValue());
        else if (rollingFileAppender.getFileNamePattern().trim().isEmpty())
            throw new EmptyPatternException(LogExeptionCodeConstant.EMPTYPATTERNEXCEPTION.getValue(),
                    LogExeptionCodeConstant.EMPTYPATTERNEXCEPTIONMESSAGEEMPTY.getValue());
        else if (!rollingFileAppender.getFileNamePattern().contains("%d"))
            throw new BaseUncheckedException(LogExeptionCodeConstant.PATTERNSYNTAXEXCEPTION.getValue(),
                    LogExeptionCodeConstant.PATTERNSYNTAXEXCEPTIONMESSAGED.getValue());
        else if (!rollingFileAppender.getMaxFileSize().trim().isEmpty() && rollingFileAppender.getMaxFileSize() != null
                && !rollingFileAppender.getFileNamePattern().contains("%i"))
            throw new BaseUncheckedException(LogExeptionCodeConstant.PATTERNSYNTAXEXCEPTION.getValue(),
                    LogExeptionCodeConstant.PATTERNSYNTAXEXCEPTIONMESSAGEI.getValue());
        else if (rollingFileAppender.getFileName() == null)
            throw new FileNameNotProvided(LogExeptionCodeConstant.FILENAMENOTPROVIDED.getValue(),
                    LogExeptionCodeConstant.FILENAMENOTPROVIDEDMESSAGENULL.getValue());
        else if (rollingFileAppender.getFileName().trim().isEmpty())
            throw new FileNameNotProvided(LogExeptionCodeConstant.FILENAMENOTPROVIDED.getValue(),
                    LogExeptionCodeConstant.FILENAMENOTPROVIDEDMESSAGEEMPTY.getValue());
        else if (name.trim().isEmpty())
            throw new ClassNameNotFoundException(LogExeptionCodeConstant.CLASSNAMENOTFOUNDEXEPTION.getValue(),
                    LogExeptionCodeConstant.CLASSNAMENOTFOUNDEXEPTIONMESSAGE.getValue());
        else
            try {
                return new LoggerImpl(rollingFileAppender, name, loglevel);
            } catch (IllegalStateException e) {
                throw new IllegalStateException(LogExeptionCodeConstant.ILLEGALSTATEEXCEPTION.getValue(),
                        LogExeptionCodeConstant.ILLEGALSTATEEXCEPTIONMESSAGE.getValue(), e);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(LogExeptionCodeConstant.ILLEGALSTATEEXCEPTION.getValue(),
                        LogExeptionCodeConstant.ILLEGALSTATEEXCEPTIONMESSAGE.getValue(), e);
            }
    }

    /*
     * (non-Javadoc)
     *
     * @see Logger#debug(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void debug(String sessionId, String idType, String id, String description) {
        logger.debug(LOGDISPLAY, sessionId, idType, id, description);

    }

    /*
     * (non-Javadoc)
     *
     * @see Logger#warn(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void warn(String sessionId, String idType, String id, String description) {
        logger.warn(LOGDISPLAY, sessionId, idType, id, description);

    }

    /*
     * (non-Javadoc)
     *
     * @see Logger#error(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void error(String sessionId, String idType, String id, String description) {
        logger.error(LOGDISPLAY, sessionId, idType, id, description);

    }

    /*
     * (non-Javadoc)
     *
     * @see Logger#info(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void info(String sessionId, String idType, String id, String description) {
        logger.info(LOGDISPLAY, sessionId, idType, id, description);

    }

    /*
     * (non-Javadoc)
     *
     * @see Logger#trace(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void trace(String sessionId, String idType, String id, String description) {
        logger.trace(LOGDISPLAY, sessionId, idType, id, description);
    }

    @Override
    public void error(String s, Exception ex) {
        logger.error(s,ex);
    }

    /**
     * Configures Layout for
     *
     * @param context {@link LoggerContext} instance
     * @return {@link PatternLayoutEncoder} instance
     */
    private PatternLayoutEncoder getdefaultPattern(LoggerContext context) {
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern(ConfigurationDefault.LOGPATTERN);
        ple.setContext(context);
        ple.start();
        return ple;
    }

    /**
     * Stop an appender
     *
     * @param appenderName name of the appender
     */
    public static void stop(String appenderName) {
        if (fileAppenders.containsKey(appenderName)) {
            fileAppenders.get(appenderName).stop();
            fileAppenders.remove(appenderName);
        } else if (rollingFileAppenders.containsKey(appenderName)) {
            rollingFileAppenders.get(appenderName).stop();
            rollingFileAppenders.remove(appenderName);
        }
    }

    /**
     * Stop all appenders
     */
    public static void stopAll() {
        rollingFileAppenders.values().forEach(Appender<ILoggingEvent>::stop);
        fileAppenders.values().forEach(Appender<ILoggingEvent>::stop);
    }
}