package com.tutuka.lib.logger.factory;


import com.tutuka.lib.logger.appender.ConsoleAppender;
import com.tutuka.lib.logger.appender.FileAppender;
import com.tutuka.lib.logger.appender.RollingFileAppender;
import com.tutuka.lib.logger.util.LogExeptionCodeConstant;
import com.tutuka.lib.logger.util.LogLevel;
import com.tutuka.lib.logger.util.LoggerMethod;
import com.tutuka.lib.logger.LoggerImpl;
import com.tutuka.lib.logger.exception.ImplementationNotFound;
import com.tutuka.lib.logger.Logger;
import com.tutuka.lib.logger.util.LoggerUtils;

import java.io.File;

/**
 * Factory class for
 */
public class Logfactory {

    /**
     * Default constructor for this class
     */
    private Logfactory() {

    }

    /**
     * Default Console factory method to configure logger
     *
     * @param consoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param clazz                reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultConsoleLogger(ConsoleAppender consoleAppender, Class<?> clazz) {
        return LoggerImpl.getConsoleLogger(consoleAppender, clazz.getName(), null);
    }

    /**
     * Default Console factory method to configure logger
     *
     * @param ConsoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param clazz                reference of the calling class
     * @param logLevel             {@link LogLevel} more logger
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultConsoleLogger(ConsoleAppender ConsoleAppender, Class<?> clazz,
                                                 LogLevel logLevel) {
        return LoggerImpl.getConsoleLogger(ConsoleAppender, clazz.getName(), logLevel);
    }

    /**
     * Default Console factory method to configure logger
     *
     * @param ConsoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param name                 name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultConsoleLogger(ConsoleAppender ConsoleAppender, String name) {
        return LoggerImpl.getConsoleLogger(ConsoleAppender, name, null);
    }

    /**
     * Default Console factory method to configure logger
     *
     * @param ConsoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param name                 name of the calling class
     * @param logLevel             {@link LogLevel} more logger
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultConsoleLogger(ConsoleAppender ConsoleAppender, String name, LogLevel logLevel) {
        return LoggerImpl.getConsoleLogger(ConsoleAppender, name, logLevel);
    }

    /**
     * Default File factory method to configure logger
     *
     * @param fileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param clazz             reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultFileLogger(FileAppender fileAppender, Class<?> clazz) {
        return LoggerImpl.getFileLogger(fileAppender, clazz.getName(), null);
    }

    /**
     * Default File factory method to configure logger
     *
     * @param FileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param clazz             reference of the calling class
     * @param logLevel          {@link LogLevel} more logger
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultFileLogger(FileAppender FileAppender, Class<?> clazz, LogLevel logLevel) {
        return LoggerImpl.getFileLogger(FileAppender, clazz.getName(), logLevel);
    }

    /**
     * Default File factory method to configure logger
     *
     * @param FileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param name              name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultFileLogger(FileAppender FileAppender, String name) {
        return LoggerImpl.getFileLogger(FileAppender, name, null);
    }

    /**
     * Default File factory method to configure logger
     *
     * @param FileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param name              name of the calling class
     * @param logLevel          {@link LogLevel} more logger
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultFileLogger(FileAppender FileAppender, String name, LogLevel logLevel) {
        return LoggerImpl.getFileLogger(FileAppender, name, logLevel);
    }

    /**
     * Default Rolling file factory method to configure logger
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param clazz                    reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultRollingFileLogger(RollingFileAppender RollingFileAppender, Class<?> clazz) {
        return LoggerImpl.getRollingFileLogger(RollingFileAppender, clazz.getName(), null);
    }

    /**
     * Default Rolling file factory method to configure logger
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param clazz                    reference of the calling class
     * @param logLevel                 {@link LogLevel} more logger
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultRollingFileLogger(RollingFileAppender RollingFileAppender, Class<?> clazz,
                                                     LogLevel logLevel) {
        return LoggerImpl.getRollingFileLogger(RollingFileAppender, clazz.getName(), logLevel);
    }

    /**
     * Default Rolling file factory method to configure logger
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param name                     name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultRollingFileLogger(RollingFileAppender RollingFileAppender, String name) {
        return LoggerImpl.getRollingFileLogger(RollingFileAppender, name, null);
    }

    /**
     * Default Rolling file factory method to configure logger
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param name                     name of the calling class
     * @param logLevel                 {@link LogLevel} more logger
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultRollingFileLogger(RollingFileAppender RollingFileAppender, String name,
                                                     LogLevel logLevel) {
        return LoggerImpl.getRollingFileLogger(RollingFileAppender, name, logLevel);
    }

    /**
     * Console factory method to configure logger
     *
     * @param ConsoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param LoggerMethod    type of Logging implementation
     * @param clazz                reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getConsoleLogger(ConsoleAppender ConsoleAppender, LoggerMethod LoggerMethod,
                                          Class<?> clazz) {
        if (LoggerMethod == LoggerMethod.LOGBACK) {
            return LoggerImpl.getConsoleLogger(ConsoleAppender, clazz.getName(), null);
        } else {
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
        }
    }

    /**
     * Console factory method to configure logger
     *
     * @param ConsoleAppender {@link ConsoleAppender} instance which contains
     *                             all configurations
     * @param LoggerMethod    type of Logging implementation
     * @param name                 name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getConsoleLogger(ConsoleAppender ConsoleAppender, LoggerMethod LoggerMethod,
                                          String name) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getConsoleLogger(ConsoleAppender, name, null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * File factory method to configure logger
     *
     * @param FileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param LoggerMethod type of Logging implementation
     * @param clazz             reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getFileLogger(FileAppender FileAppender, LoggerMethod LoggerMethod, Class<?> clazz) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getFileLogger(FileAppender, clazz.getName(), null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * File factory method to configure logger
     *
     * @param FileAppender {@link FileAppender} instance which contains all
     *                          configurations
     * @param LoggerMethod type of Logging implementation
     * @param name              name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getFileLogger(FileAppender FileAppender, LoggerMethod LoggerMethod, String name) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getFileLogger(FileAppender, name, null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * Rolling file factory method to configure logger
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param LoggerMethod        type of Logging implementation
     * @param clazz                    reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getRollingFileLogger(RollingFileAppender RollingFileAppender,
                                              LoggerMethod LoggerMethod, Class<?> clazz) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getRollingFileLogger(RollingFileAppender, clazz.getName(), null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * Rolling file factory method to configure logger
     *
     * @param RollingFileAppender {@link RollingFileAppender} instance which
     *                                 contains all configurations
     * @param LoggerMethod        type of Logging implementation
     * @param name                     name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getRollingFileLogger(RollingFileAppender RollingFileAppender,
                                              LoggerMethod LoggerMethod, String name) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getRollingFileLogger(RollingFileAppender, name, null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * Default Console factory method to configure logger
     *
     * @param ConsoleAppenderFile XML file containing  console logger
     *                                 configurations
     * @param clazz                    reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultConsoleLogger(File ConsoleAppenderFile, Class<?> clazz) {
        return LoggerImpl.getConsoleLogger(
                (ConsoleAppender) LoggerUtils.unmarshall(ConsoleAppenderFile, ConsoleAppender.class),
                clazz.getName(), null);
    }

    /**
     * Default Console factory method to configure logger
     *
     * @param ConsoleAppenderFile XML file containing  console logger
     *                                 configurations
     * @param name                     name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultConsoleLogger(File ConsoleAppenderFile, String name) {
        return LoggerImpl.getConsoleLogger(
                (ConsoleAppender) LoggerUtils.unmarshall(ConsoleAppenderFile, ConsoleAppender.class), name, null);
    }

    /**
     * Default File factory method to configure logger
     *
     * @param FileAppenderFile XML file containing  file logger
     *                              configurations
     * @param clazz                 reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultFileLogger(File FileAppenderFile, Class<?> clazz) {
        return LoggerImpl.getFileLogger(
                (FileAppender) LoggerUtils.unmarshall(FileAppenderFile, FileAppender.class), clazz.getName(),
                null);
    }

    /**
     * Default File factory method to configure logger
     *
     * @param FileAppenderFile XML file containing  file logger
     *                              configurations
     * @param name                  name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultFileLogger(File FileAppenderFile, String name) {
        return LoggerImpl.getFileLogger(
                (FileAppender) LoggerUtils.unmarshall(FileAppenderFile, FileAppender.class), name, null);
    }

    /**
     * Default Rolling file factory method to configure logger
     *
     * @param RollingFileAppenderFile XML file containing  rolling file
     *                                     logger configurations
     * @param clazz                        reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultRollingFileLogger(File RollingFileAppenderFile, Class<?> clazz) {
        return LoggerImpl.getRollingFileLogger(
                (RollingFileAppender) LoggerUtils.unmarshall(RollingFileAppenderFile, RollingFileAppender.class),
                clazz.getName(), null);
    }

    /**
     * Default Rolling file factory method to configure logger
     *
     * @param RollingFileAppenderFile XML file containing  rolling file
     *                                     logger configurations
     * @param name                         name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getDefaultRollingFileLogger(File RollingFileAppenderFile, String name) {
        return LoggerImpl.getRollingFileLogger(
                (RollingFileAppender) LoggerUtils.unmarshall(RollingFileAppenderFile, RollingFileAppender.class),
                name, null);
    }

    /**
     * Console factory method to configure logger
     *
     * @param ConsoleAppenderFile XML file containing  console logger
     *                                 configurations
     * @param LoggerMethod        type of Logging implementation
     * @param clazz                    reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getConsoleLogger(File ConsoleAppenderFile, LoggerMethod LoggerMethod,
                                          Class<?> clazz) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getConsoleLogger(
                    (ConsoleAppender) LoggerUtils.unmarshall(ConsoleAppenderFile, ConsoleAppender.class),
                    clazz.getName(), null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * Console factory method to configure logger
     *
     * @param ConsoleAppenderFile XML file containing  console logger
     *                                 configurations
     * @param LoggerMethod        type of Logging implementation
     * @param name                     name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getConsoleLogger(File ConsoleAppenderFile, LoggerMethod LoggerMethod, String name) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getConsoleLogger(
                    (ConsoleAppender) LoggerUtils.unmarshall(ConsoleAppenderFile, ConsoleAppender.class), name,
                    null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * File factory method to configure logger
     *
     * @param FileAppenderFile XML file containing  file logger
     *                              configurations
     * @param LoggerMethod     type of Logging implementation
     * @param clazz                 reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getFileLogger(File FileAppenderFile, LoggerMethod LoggerMethod, Class<?> clazz) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getFileLogger(
                    (FileAppender) LoggerUtils.unmarshall(FileAppenderFile, FileAppender.class), clazz.getName(),
                    null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * File factory method to configure logger
     *
     * @param FileAppenderFile XML file containing  file logger
     *                              configurations
     * @param LoggerMethod     type of Logging implementation
     * @param name                  name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getFileLogger(File FileAppenderFile, LoggerMethod LoggerMethod, String name) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getFileLogger(
                    (FileAppender) LoggerUtils.unmarshall(FileAppenderFile, FileAppender.class), name, null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * Rolling file factory method to configure logger
     *
     * @param RollingFileAppenderFile XML file containing  rolling file
     *                                     logger configurations
     * @param LoggerMethod            type of Logging implementation
     * @param clazz                        reference of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getRollingFileLogger(File RollingFileAppenderFile, LoggerMethod LoggerMethod,
                                              Class<?> clazz) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getRollingFileLogger((RollingFileAppender) LoggerUtils
                    .unmarshall(RollingFileAppenderFile, RollingFileAppender.class), clazz.getName(), null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    /**
     * Rolling file factory method to configure logger
     *
     * @param RollingFileAppenderFile XML file containing  rolling file
     *                                     logger configurations
     * @param LoggerMethod            type of Logging implementation
     * @param name                         name of the calling class
     * @return configured {@link Logger} instance
     */
    public static Logger getRollingFileLogger(File RollingFileAppenderFile, LoggerMethod LoggerMethod,
                                              String name) {
        if (LoggerMethod == LoggerMethod.LOGBACK)
            return LoggerImpl.getRollingFileLogger((RollingFileAppender) LoggerUtils
                    .unmarshall(RollingFileAppenderFile, RollingFileAppender.class), name, null);
        else
            throw new ImplementationNotFound(LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUND.getValue(),
                    LogExeptionCodeConstant.IMPLEMENTATIONNOTFOUNDMESSAGE.getValue());
    }

    public static void stop(String appendersName) {
        LoggerImpl.stop(appendersName);
    }

    public static void stopAll() {
        LoggerImpl.stopAll();
    }

}